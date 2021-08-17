package zy.nav.register

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import kotlin.Unit
import kotlin.jvm.functions.Function1
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KaptAnnotationProcessorOptions
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import zy.nav.doc.NavDoc

class PluginLaunch implements Plugin<Project> {

    @Override
    void apply(Project project) {
        Logger.make(project)
        Logger.i('Project enable nav-register plugin')
        //apply plugin
        if (!project.plugins.hasPlugin("kotlin-android")) {
            project.plugins.apply("kotlin-android")
        }
        if (!project.plugins.hasPlugin("kotlin-kapt")) {
            project.plugins.apply("kotlin-kapt")
        }
        //add dependencies
        def apiVersion = '1.0.0'
        project.dependencies.add('implementation', "zy.nav:nav:$apiVersion")
        project.dependencies.add('androidTestImplementation', "zy.nav:test:$apiVersion")
        project.dependencies.add('kapt', "zy.nav:compiler:$apiVersion")
        //add kapt extension
        def isApp = project.plugins.hasPlugin(AppPlugin)
        def android = isApp
                ? project.extensions.getByType(AppExtension)
                : project.extensions.getByType(LibraryExtension)
        def kapt = project.extensions.findByType(KaptExtension)
        kapt.arguments(new Function1<KaptAnnotationProcessorOptions, Unit>() {
            @Override
            Unit invoke(KaptAnnotationProcessorOptions kaptAnnotationProcessorOptions) {
                kaptAnnotationProcessorOptions.arg('MODULE_NAME', project.name)
                kaptAnnotationProcessorOptions.arg('DOC_DIR',
                        android.sourceSets.maybeCreate("main").assets.srcDirs[0])
                return null
            }
        })
        //asm code
        if (isApp) {
            def transformImpl = new RegisterTransform(project)
            ArrayList<ScanSetting> list = new ArrayList<>(5)
            list.add(new ScanSetting('zy/nav/NavRegistry',
                    'zy/nav/generate/',
                    'zy/nav/ActivityRegister'))
            list.add(new ScanSetting('zy/nav/NavRegistry',
                    'zy/nav/generate/',
                    'zy/nav/FragmentRegister'))
            list.add(new ScanSetting('zy/nav/NavRegistry',
                    'zy/nav/generate/',
                    'zy/nav/ServiceRegister'))
            list.add(new ScanSetting('zy/nav/NavRegistry',
                    'zy/nav/generate/',
                    'zy/nav/InterceptorRegister'))
            //auto register for holder
            list.add(new ScanSetting('zy/holder/OneAdapter',
                    'zy/holder/generate/',
                    'zy/holder/HolderRegister'))
            RegisterTransform.registerList = list
            //register this plugin
            android.registerTransform(transformImpl)

            project.extensions.create("nav", NavExtension)
            merge(project, android as AppExtension)
        }
    }

    static void merge(def project, def android) {
        android.applicationVariants.all { variant ->
            def mergeAssets = variant.mergeAssetsProvider.get()
            mergeAssets.doLast {
                def srcDir = "${mergeAssets.outputDir.get()}/nav"
                def dstDir = "$project.buildDir/nav"
                def nav = project.extensions.findByType(NavExtension)
                NavDoc.merge(srcDir, dstDir, nav.serviceFilters)
            }
        }
    }

}
