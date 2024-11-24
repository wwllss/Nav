package zy.nav;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import zy.nav.exception.NavException;

class SystemFindInterceptor implements Interceptor {

    SystemFindInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws NavException {
        Request request = chain.request();
        Response response = chain.process(request);
        final PackageManager manager = request.context().getPackageManager();
        final String packageName = request.context().getPackageName();
        if (NavAction.ACTIVITY == request.action()
                && !response.success()) {
            Intent intent = new Intent();
            intent.setPackage(packageName);
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            String url = request.url();
            intent.setData(Uri.parse(url));
            @SuppressLint("QueryPermissionsNeeded") ComponentName componentName = intent.resolveActivity(manager);
            if (componentName != null) {
                response.target(componentName.getClassName());
            }
        }
        return response;
    }

}
