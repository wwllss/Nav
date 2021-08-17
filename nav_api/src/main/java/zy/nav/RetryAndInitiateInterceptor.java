package zy.nav;

import android.content.Intent;
import android.net.Uri;

import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

import java.util.Set;

import zy.nav.exception.NavException;

class RetryAndInitiateInterceptor implements Interceptor {

    private static final int MAX_RETRY_COUNT = 1;

    private int count;

    RetryAndInitiateInterceptor() {
        this.count = 0;
    }

    @Override
    public Response intercept(Chain chain) throws NavException {
        Request request = chain.request();
        String url = request.url();
        Utils.requireUrlNotEmpty(url);
        if (++count > MAX_RETRY_COUNT + 1) {
            throw new NavException("retry count is "
                    + MAX_RETRY_COUNT + "time,And it's exhausted.");
        }
        Uri uri = Uri.parse(url);
        Set<String> names = uri.getQueryParameterNames();
        if (!Utils.isEmpty(names)) {
            for (String name : names) {
                request.params().putString(name, uri.getQueryParameter(name));
            }
        }
        Response response = chain.process(request);
        if (response.success()) {
            Class<?> findClass = response.foundClass();
            if (NavAction.ACTIVITY == request.action()) {
                ActivityOptionsCompat optionsCompat = request.options();
                Intent intent = new Intent(request.context(), findClass);
                intent.putExtras(response.params());
                intent.addFlags(request.flags());
                request.initiator().startActivityForResult(intent,
                        request.requestCode(), optionsCompat == null ? null : optionsCompat.toBundle());
            }
            if (NavAction.FRAGMENT == request.action()) {
                try {
                    Fragment fragment = (Fragment) findClass.newInstance();
                    fragment.setArguments(response.extras());
                    response.fragment(fragment);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return response;
    }
}
