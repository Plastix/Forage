package io.github.plastix.forage.ui.log;

import javax.inject.Inject;

import io.github.plastix.forage.R;
import io.github.plastix.forage.data.api.OkApiInteractor;
import io.github.plastix.forage.data.api.response.SubmitLogResponse;
import io.github.plastix.forage.data.network.NetworkInteractor;
import io.github.plastix.forage.ui.base.rx.RxPresenter;
import retrofit2.adapter.rxjava.HttpException;
import rx.SingleSubscriber;
import rx.functions.Action0;
import rx.functions.Action1;

public class LogPresenter extends RxPresenter<LogView> {

    private OkApiInteractor okApiInteractor;
    private NetworkInteractor networkInteractor;

    @Inject
    public LogPresenter(OkApiInteractor okApiInteractor, NetworkInteractor networkInteractor) {
        this.okApiInteractor = okApiInteractor;
        this.networkInteractor = networkInteractor;
    }

    public void submitLog(final String cacheCode, final String comment, final String type) {
        networkInteractor.hasInternetConnectionCompletable()
                .subscribe(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (isViewAttached()) {
                            view.showErrorInternetDialog();
                        }
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        addSubscription(
                                okApiInteractor.submitLog(cacheCode, type, comment)
                                        .toObservable()
                                        .compose(LogPresenter.this.<SubmitLogResponse>deliverFirst())
                                        .toSingle()
                                        .doOnSubscribe(new Action0() {
                                            @Override
                                            public void call() {
                                                if (isViewAttached()) {
                                                    view.showSubmittingDialog();
                                                }
                                            }
                                        })
                                        .subscribe(new SingleSubscriber<SubmitLogResponse>() {
                                            @Override
                                            public void onSuccess(SubmitLogResponse response) {
                                                if (isViewAttached()) {
                                                    if (response.isSuccessful) {
                                                        view.showSuccessfulSubmit();
                                                    } else {
                                                        view.showErrorDialog(response.message);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onError(Throwable error) {
                                                if (isViewAttached()) {
                                                    // Non-200 HTTP Code
                                                    if (error instanceof HttpException) {
                                                        HttpException httpException = ((HttpException) error);
                                                        view.showErrorDialog(httpException.getMessage());
                                                    } else {
                                                        view.showErrorDialog(R.string.log_submit_error_unknown);
                                                    }
                                                }
                                            }
                                        })
                        );
                    }
                });
    }

    @Override
    public void onDestroyed() {
        // No op
    }
}
