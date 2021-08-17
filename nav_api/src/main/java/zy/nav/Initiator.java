package zy.nav;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;

interface Initiator {

    Context context();

    void startActivityForResult(Intent intent, int requestCode, Bundle option);

    class Factory {

        static Initiator from(Context context) {
            if (context instanceof Activity) {
                return from((Activity) context);
            }
            return new ContextInitiator(context);
        }

        static Initiator from(Activity activity) {
            return new ActivityInitiator(activity);
        }

        static Initiator from(Fragment fragment) {
            return new FragmentInitiator(fragment);
        }
    }

    class ContextInitiator implements Initiator {

        final WeakReference<Context> context;

        @Override
        public Context context() {
            Context context = this.context.get();
            if (context == null) {
                throw new NullPointerException("context == null");
            }
            return context;
        }

        ContextInitiator(Context context) {
            if (context == null) {
                throw new NullPointerException("context == null");
            }
            this.context = new WeakReference<>(context);
        }

        @Override
        public void startActivityForResult(Intent intent, int requestCode, Bundle option) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context().startActivity(intent);
        }
    }

    class ActivityInitiator extends ContextInitiator {

        ActivityInitiator(Activity activity) {
            super(activity);
        }

        @Override
        public void startActivityForResult(Intent intent, int requestCode, Bundle option) {
            Activity activity = (Activity) context();
            if (option != null) {
                activity.startActivityForResult(intent, requestCode, option);
            } else {
                activity.startActivityForResult(intent, requestCode);
            }
        }
    }

    class FragmentInitiator extends ContextInitiator {

        final WeakReference<Fragment> fragment;

        FragmentInitiator(Fragment fragment) {
            super(fragment.getContext());
            this.fragment = new WeakReference<>(fragment);
        }

        @Override
        public void startActivityForResult(Intent intent, int requestCode, Bundle option) {
            Fragment fragment = this.fragment.get();
            if (fragment == null) {
                throw new NullPointerException("fragment == null");
            }
            if (option != null) {
                fragment.startActivityForResult(intent, requestCode, option);
            } else {
                fragment.startActivityForResult(intent, requestCode);
            }
        }
    }

}
