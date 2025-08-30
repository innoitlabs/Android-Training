package com.example.mvvmarchitecture.databinding;
import com.example.mvvmarchitecture.R;
import com.example.mvvmarchitecture.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ItemUserBindingImpl extends ItemUserBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.userAvatar, 4);
    }
    // views
    @NonNull
    private final com.google.android.material.card.MaterialCardView mboundView0;
    @NonNull
    private final android.widget.TextView mboundView1;
    @NonNull
    private final android.widget.TextView mboundView2;
    @NonNull
    private final android.widget.TextView mboundView3;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ItemUserBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 5, sIncludes, sViewsWithIds));
    }
    private ItemUserBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.ImageView) bindings[4]
            );
        this.mboundView0 = (com.google.android.material.card.MaterialCardView) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (android.widget.TextView) bindings[1];
        this.mboundView1.setTag(null);
        this.mboundView2 = (android.widget.TextView) bindings[2];
        this.mboundView2.setTag(null);
        this.mboundView3 = (android.widget.TextView) bindings[3];
        this.mboundView3.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.user == variableId) {
            setUser((com.example.mvvmarchitecture.User) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setUser(@Nullable com.example.mvvmarchitecture.User User) {
        this.mUser = User;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.user);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        com.example.mvvmarchitecture.User user = mUser;
        java.lang.String userPhone = null;
        int userPhoneLength = 0;
        int userPhoneJavaLangObjectNullUserPhoneLengthInt0BooleanFalseAndroidViewViewVISIBLEAndroidViewViewGONE = 0;
        java.lang.String userName = null;
        java.lang.String userEmail = null;
        boolean userPhoneJavaLangObjectNullUserPhoneLengthInt0BooleanFalse = false;
        boolean userPhoneJavaLangObjectNull = false;
        boolean userPhoneLengthInt0 = false;

        if ((dirtyFlags & 0x3L) != 0) {



                if (user != null) {
                    // read user.phone
                    userPhone = user.getPhone();
                    // read user.name
                    userName = user.getName();
                    // read user.email
                    userEmail = user.getEmail();
                }


                // read user.phone != null
                userPhoneJavaLangObjectNull = (userPhone) != (null);
            if((dirtyFlags & 0x3L) != 0) {
                if(userPhoneJavaLangObjectNull) {
                        dirtyFlags |= 0x20L;
                }
                else {
                        dirtyFlags |= 0x10L;
                }
            }
        }
        // batch finished

        if ((dirtyFlags & 0x20L) != 0) {

                if (userPhone != null) {
                    // read user.phone.length()
                    userPhoneLength = userPhone.length();
                }


                // read user.phone.length() > 0
                userPhoneLengthInt0 = (userPhoneLength) > (0);
        }

        if ((dirtyFlags & 0x3L) != 0) {

                // read user.phone != null ? user.phone.length() > 0 : false
                userPhoneJavaLangObjectNullUserPhoneLengthInt0BooleanFalse = ((userPhoneJavaLangObjectNull) ? (userPhoneLengthInt0) : (false));
            if((dirtyFlags & 0x3L) != 0) {
                if(userPhoneJavaLangObjectNullUserPhoneLengthInt0BooleanFalse) {
                        dirtyFlags |= 0x8L;
                }
                else {
                        dirtyFlags |= 0x4L;
                }
            }


                // read user.phone != null ? user.phone.length() > 0 : false ? android.view.View.VISIBLE : android.view.View.GONE
                userPhoneJavaLangObjectNullUserPhoneLengthInt0BooleanFalseAndroidViewViewVISIBLEAndroidViewViewGONE = ((userPhoneJavaLangObjectNullUserPhoneLengthInt0BooleanFalse) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView1, userName);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView2, userEmail);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView3, userPhone);
            this.mboundView3.setVisibility(userPhoneJavaLangObjectNullUserPhoneLengthInt0BooleanFalseAndroidViewViewVISIBLEAndroidViewViewGONE);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): user
        flag 1 (0x2L): null
        flag 2 (0x3L): user.phone != null ? user.phone.length() > 0 : false ? android.view.View.VISIBLE : android.view.View.GONE
        flag 3 (0x4L): user.phone != null ? user.phone.length() > 0 : false ? android.view.View.VISIBLE : android.view.View.GONE
        flag 4 (0x5L): user.phone != null ? user.phone.length() > 0 : false
        flag 5 (0x6L): user.phone != null ? user.phone.length() > 0 : false
    flag mapping end*/
    //end
}