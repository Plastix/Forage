package io.github.plastix.forage.ui.log;

import javax.inject.Inject;

import io.github.plastix.forage.R;
import io.github.plastix.forage.data.api.OkApiInteractor;
import io.github.plastix.forage.data.api.response.SubmitLogResponse;
import io.github.plastix.forage.data.network.NetworkInteractor;
import io.github.plastix.forage.ui.base.rx.RxPresenter;
import retrofit2.adapter.rxjava.HttpException;

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
                .subscribe(throwable -> {
                    if (isViewAttached()) {
                        view.showErrorInternetDialog();
                    }
                }, () -> addSubscription(
                        okApiInteractor.submitLog(cacheCode, type, comment)
                                .toObservable()
                                .compose(LogPresenter.this.<SubmitLogResponse>deliverFirst())
                                .toSingle()
                                .doOnSubscribe(() -> {
                                    if (isViewAttached()) {
                                        view.showSubmittingDialog();
                                    }
                                })
                                .subscribe(submitLogResponse -> {
                                            if (isViewAttached()) {
                                                if (submitLogResponse.isSuccessful) {
                                                    view.showSuccessfulSubmit();
                                                } else {
                                                    view.showErrorDialog(submitLogResponse.message);
                                                }
                                            }
                                        },
                                        throwable -> {
                                            if (isViewAttached()) {
                                                // Non-200 HTTP Code
                                                if (throwable instanceof HttpException) {
                                                    HttpException httpException = ((HttpException) throwable);
                                                    view.showErrorDialog(httpException.getMessage());
                                                } else {
                                                    view.showErrorDialog(R.string.log_submit_error_unknown);
                                                }
                                            }
                                        })
                ));
    }

    @Override
    public void onDestroyed() {
        // No op
    }
}
