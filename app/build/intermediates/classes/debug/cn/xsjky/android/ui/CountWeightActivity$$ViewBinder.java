// Generated code from Butter Knife. Do not modify!
package cn.xsjky.android.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CountWeightActivity$$ViewBinder<T extends cn.xsjky.android.ui.CountWeightActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624118, "field 'mLlProductDescriptions'");
    target.mLlProductDescriptions = finder.castView(view, 2131624118, "field 'mLlProductDescriptions'");
    view = finder.findRequiredView(source, 2131624117, "field 'mLlContianer'");
    target.mLlContianer = finder.castView(view, 2131624117, "field 'mLlContianer'");
    view = finder.findRequiredView(source, 2131624119, "field 'mLlCount'");
    target.mLlCount = finder.castView(view, 2131624119, "field 'mLlCount'");
    view = finder.findRequiredView(source, 2131624122, "field 'mSumOfCount'");
    target.mSumOfCount = finder.castView(view, 2131624122, "field 'mSumOfCount'");
    view = finder.findRequiredView(source, 2131624123, "field 'mSumOfWeight'");
    target.mSumOfWeight = finder.castView(view, 2131624123, "field 'mSumOfWeight'");
    view = finder.findRequiredView(source, 2131624121, "field 'mLlSum'");
    target.mLlSum = finder.castView(view, 2131624121, "field 'mLlSum'");
    view = finder.findRequiredView(source, 2131624120, "field 'mLlCountSum'");
    target.mLlCountSum = finder.castView(view, 2131624120, "field 'mLlCountSum'");
    view = finder.findRequiredView(source, 2131624125, "field 'mBtnOk'");
    target.mBtnOk = finder.castView(view, 2131624125, "field 'mBtnOk'");
    view = finder.findRequiredView(source, 2131624124, "field 'mCkRemark'");
    target.mCkRemark = finder.castView(view, 2131624124, "field 'mCkRemark'");
  }

  @Override public void unbind(T target) {
    target.mLlProductDescriptions = null;
    target.mLlContianer = null;
    target.mLlCount = null;
    target.mSumOfCount = null;
    target.mSumOfWeight = null;
    target.mLlSum = null;
    target.mLlCountSum = null;
    target.mBtnOk = null;
    target.mCkRemark = null;
  }
}
