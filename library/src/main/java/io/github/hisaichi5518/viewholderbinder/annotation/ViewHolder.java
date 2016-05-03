package io.github.hisaichi5518.viewholderbinder.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
public @interface ViewHolder {
    int viewType();
    int layout();
}
