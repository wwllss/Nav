package zy.nav;

import android.net.Uri;

class RouteFindInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) {
        Request request = chain.request();
        String url = request.url();
        String path = Uri.parse(url).getPath();
        String className;
        if (NavAction.ACTIVITY == request.action()) {
            className = NavRegistry.getActivity(path);
        } else {
            className = NavRegistry.getFragment(path);
        }
        Response response = Response.newResponse();
        if (!Utils.isEmpty(className)) {
            response.target(className);
        }
        return response;
    }

}
