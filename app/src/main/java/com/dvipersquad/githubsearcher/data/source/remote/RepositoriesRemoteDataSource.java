package com.dvipersquad.githubsearcher.data.source.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.dvipersquad.githubsearcher.data.Repository;
import com.dvipersquad.githubsearcher.data.source.RepositoriesDataSource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

@Singleton
public class RepositoriesRemoteDataSource implements RepositoriesDataSource {

    private final static String API_URL = "https://api.github.com/";
    private final static String SECURITY_TOKEN = "855cc35b9ceac157aaffbf53ded077d145ed75a0";
    private final static Integer REPOSITORIES_TO_FETCH = 15;
    private final static Integer WATCHERS_TO_FETCH = 10;
    private final static String TAG = RepositoriesRemoteDataSource.class.getSimpleName();
    private static final String NOT_FOUND = "Not found";
    private RepositoriesApi repositoriesApi;

    public RepositoriesRemoteDataSource() {
        // Logging interceptor to log all requests and responses
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        repositoriesApi = retrofit.create(RepositoriesApi.class);
    }

    @Override
    public void getRepositories(@NonNull String query, String lastElement,
                                @NonNull final LoadRepositoriesCallback callback) {
        GetRepositoriesApiRequest request =
                new GetRepositoriesApiRequest(
                        new GetRepositoriesApiRequest.Variables(
                                query,
                                REPOSITORIES_TO_FETCH,
                                lastElement,
                                WATCHERS_TO_FETCH));

        Call<RepositoriesApiResponse> call = repositoriesApi.getRepositories(request);
        call.enqueue(new Callback<RepositoriesApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<RepositoriesApiResponse> call,
                                   @NonNull retrofit2.Response<RepositoriesApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<Repository> repositories = new ArrayList<>();
                    for (RepositoriesApiResponse.RepositoryNode node : response.body().getData().getSearch().getRepositories()) {
                        repositories.add(new Repository(node.getNode()));
                    }
                    callback.onRepositoriesLoaded(
                            repositories,
                            response.body().getData().getSearch().getPageInfo().getEndCursor(),
                            response.body().getData().getSearch().getPageInfo().hasNextPage());
                } else {
                    Log.e(TAG, "response unsuccessful or with empty body ");
                    callback.onDataNotAvailable(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<RepositoriesApiResponse> call,
                                  @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
                callback.onDataNotAvailable(t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void getRepository(@NonNull String repositoryId,
                              @NonNull final GetRepositoryCallback callback) {
//        Call<Repository> call = repositoriesApi.getRepository();
//        call.enqueue(new Callback<Repository>() {
//            @Override
//            public void onResponse(Call<Repository> call, retrofit2.Response<Repository> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    callback.onRepositoryLoaded(response.body());
//                } else {
//                    Log.e(TAG, "response unsuccessful or with empty body ");
//                    callback.onDataNotAvailable(response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Repository> call, Throwable t) {
//                Log.e(TAG, t.getMessage());
//                callback.onDataNotAvailable(t.getLocalizedMessage());
//            }
//        });

        callback.onDataNotAvailable(NOT_FOUND);
    }

    @Override
    public void saveRepository(@NonNull Repository repository) {
        // Not needed here, only saved locally
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllRepositories() {
        // Not needed here, handled only locally
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteRepository(@NonNull String repositoryId) {
        // Not needed here, handled in RepositoriesRepository
        throw new UnsupportedOperationException();
    }

    public interface RepositoriesApi {
        @Headers("Authorization: Bearer " + SECURITY_TOKEN)
        @POST("graphql")
        Call<RepositoriesApiResponse> getRepositories(@Body GetRepositoriesApiRequest request);
    }
}
