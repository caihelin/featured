/*
 * Copyright (C) 2016 Sergej Shafarenka, www.halfbit.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.halfbit.featured.compiler;

import com.google.testing.compile.JavaFileObjects;

import org.junit.Ignore;
import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class FeatureEventTest {

    @Test public void checkOnEvent() throws Exception {

        JavaFileObject source = JavaFileObjects
                .forSourceLines("de.halfbit.featured.test.TestFeature",
                        "",
                        "package de.halfbit.featured.test;",
                        "import de.halfbit.featured.FeatureEvent;",
                        "import de.halfbit.featured.Feature;",
                        "public class TestFeature extends Feature<TestFeatureHost> {",
                        "    @FeatureEvent protected void onStart() { }",
                        "}"
                );

        JavaFileObject expectedSource = JavaFileObjects
                .forSourceLines("de.halfbit.featured.test.TestFeatureHost",
                        "",
                        "package de.halfbit.featured.test;",
                        "import android.content.Context;",
                        "import android.support.annotation.NonNull;",
                        "import de.halfbit.featured.FeatureHost;",
                        "public class TestFeatureHost extends FeatureHost<TestFeature, TestFeatureHost> {",
                        "    public TestFeatureHost(@NonNull Context context) {",
                        "        super(context);",
                        "    }",
                        "    public void dispatchOnStart() {",
                        "        dispatch(new OnStartEvent());",
                        "    }",
                        "    private static final class OnStartEvent extends FeatureHost.Event<TestFeature> {",
                        "        @Override protected void dispatch(TestFeature feature) {",
                        "            feature.onStart();",
                        "        }",
                        "    }",
                        "}"
                );

        assertAbout(javaSource()).that(source)
                .processedWith(new FeatureProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedSource);
    }

    @Test public void checkOnEventWithParameters() throws Exception {

        JavaFileObject source = JavaFileObjects
                .forSourceLines("de.halfbit.featured.test.TestFeature",
                        "",
                        "package de.halfbit.featured.test;",
                        "import de.halfbit.featured.FeatureEvent;",
                        "import de.halfbit.featured.Feature;",
                        "public class TestFeature extends Feature<TestFeatureHost> {",
                        "    @FeatureEvent protected void onStart(long time, boolean valid, int count, Object state) { }",
                        "}"
                );

        JavaFileObject expectedSource = JavaFileObjects
                .forSourceLines("de.halfbit.featured.test.TestFeatureHost",
                        "",
                        "package de.halfbit.featured.test;",
                        "import android.content.Context;",
                        "import android.support.annotation.NonNull;",
                        "import de.halfbit.featured.FeatureHost;",
                        "public class TestFeatureHost extends FeatureHost<TestFeature, TestFeatureHost> {",
                        "    public TestFeatureHost(@NonNull Context context) {",
                        "        super(context);",
                        "    }",
                        "    public void dispatchOnStart(long time, boolean valid, int count, Object state) {",
                        "        dispatch(new OnStartEvent(time, valid, count, state));",
                        "    }",
                        "    private static final class OnStartEvent extends FeatureHost.Event<TestFeature> {",
                        "        private final long mTime;",
                        "        private final boolean mValid;",
                        "        private final int mCount;",
                        "        private final Object mState;",
                        "        OnStartEvent(long time, boolean valid, int count, Object state) {",
                        "            mTime = time;",
                        "            mValid = valid;",
                        "            mCount = count;",
                        "            mState = state;",
                        "        }",
                        "        @Override protected void dispatch(TestFeature feature) {",
                        "            feature.onStart(mTime, mValid, mCount, mState);",
                        "        }",
                        "    }",
                        "}"
                );

        assertAbout(javaSource()).that(source)
                .processedWith(new FeatureProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedSource);
    }

    @Test public void checkOnEventWithParametersGenerics() throws Exception {

        JavaFileObject source = JavaFileObjects
                .forSourceLines("de.halfbit.featured.test.TestFeature",
                        "",
                        "package de.halfbit.featured.test;",
                        "import de.halfbit.featured.FeatureEvent;",
                        "import de.halfbit.featured.Feature;",
                        "import java.util.List;",
                        "public class TestFeature extends Feature<TestFeatureHost> {",
                        "    @FeatureEvent protected void onStart(List<String> names) { }",
                        "}"
                );

        JavaFileObject expectedSource = JavaFileObjects
                .forSourceLines("de.halfbit.featured.test.TestFeatureHost",
                        "",
                        "package de.halfbit.featured.test;",
                        "import android.content.Context;",
                        "import android.support.annotation.NonNull;",
                        "import de.halfbit.featured.FeatureHost;",
                        "import java.util.List;",
                        "public class TestFeatureHost extends FeatureHost<TestFeature, TestFeatureHost> {",
                        "    public TestFeatureHost(@NonNull Context context) {",
                        "        super(context);",
                        "    }",
                        "    public void dispatchOnStart(List<String> names) {",
                        "        dispatch(new OnStartEvent(names));",
                        "    }",
                        "    private static final class OnStartEvent extends FeatureHost.Event<TestFeature> {",
                        "        private final List<String> mNames;",
                        "        OnStartEvent(List<String> names) {",
                        "            mNames = names;",
                        "        }",
                        "        @Override protected void dispatch(TestFeature feature) {",
                        "            feature.onStart(mNames);",
                        "        }",
                        "    }",
                        "}"
                );

        assertAbout(javaSource()).that(source)
                .processedWith(new FeatureProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedSource);
    }

    @Test public void checkOnEventDispatchCompleted() throws Exception {

        JavaFileObject source = JavaFileObjects
                .forSourceLines("de.halfbit.featured.test.TestFeature",
                        "",
                        "package de.halfbit.featured.test;",
                        "import de.halfbit.featured.FeatureEvent;",
                        "import de.halfbit.featured.Feature;",
                        "public class TestFeature extends Feature<TestFeatureHost> {",
                        "    @FeatureEvent(dispatchCompleted = true) protected void onStart() { }",
                        "}"
                );

        JavaFileObject expectedSource = JavaFileObjects
                .forSourceLines("de.halfbit.featured.test.TestFeatureHost",
                        "",
                        "package de.halfbit.featured.test;",
                        "import android.content.Context;",
                        "import android.support.annotation.NonNull;",
                        "import de.halfbit.featured.FeatureHost;",
                        "public class TestFeatureHost extends FeatureHost<TestFeature, TestFeatureHost> {",
                        "    public TestFeatureHost(@NonNull Context context) {",
                        "        super(context);",
                        "    }",
                        "    public void dispatchOnStart(FeatureHost.OnDispatchCompleted onDispatchCompleted) {",
                        "        dispatch(new OnStartEvent(onDispatchCompleted));",
                        "    }",
                        "    private static final class OnStartEvent extends FeatureHost.Event<TestFeature> {",
                        "        OnStartEvent(FeatureHost.OnDispatchCompleted onDispatchCompleted) {",
                        "            mOnDispatchCompleted = onDispatchCompleted;",
                        "        }",
                        "        @Override protected void dispatch(TestFeature feature) {",
                        "            feature.onStart();",
                        "        }",
                        "    }",
                        "}"
                );

        assertAbout(javaSource()).that(source)
                .processedWith(new FeatureProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedSource);
    }

    @Test public void checkOnEventDispatchCompletedWithParameters() throws Exception {

        JavaFileObject source = JavaFileObjects
                .forSourceLines("de.halfbit.featured.test.TestFeature",
                        "",
                        "package de.halfbit.featured.test;",
                        "import de.halfbit.featured.FeatureEvent;",
                        "import de.halfbit.featured.Feature;",
                        "public class TestFeature extends Feature<TestFeatureHost> {",
                        "    @FeatureEvent(dispatchCompleted = true) protected void onStart(int time) { }",
                        "}"
                );

        JavaFileObject expectedSource = JavaFileObjects
                .forSourceLines("de.halfbit.featured.test.TestFeatureHost",
                        "",
                        "package de.halfbit.featured.test;",
                        "import android.content.Context;",
                        "import android.support.annotation.NonNull;",
                        "import de.halfbit.featured.FeatureHost;",
                        "public class TestFeatureHost extends FeatureHost<TestFeature, TestFeatureHost> {",
                        "    public TestFeatureHost(@NonNull Context context) {",
                        "        super(context);",
                        "    }",
                        "    public void dispatchOnStart(int time, FeatureHost.OnDispatchCompleted onDispatchCompleted) {",
                        "        dispatch(new OnStartEvent(time, onDispatchCompleted));",
                        "    }",
                        "    private static final class OnStartEvent extends FeatureHost.Event<TestFeature> {",
                        "        private final int mTime;",
                        "        OnStartEvent(int time, FeatureHost.OnDispatchCompleted onDispatchCompleted) {",
                        "            mTime = time;",
                        "            mOnDispatchCompleted = onDispatchCompleted;",
                        "        }",
                        "        @Override protected void dispatch(TestFeature feature) {",
                        "            feature.onStart(mTime);",
                        "        }",
                        "    }",
                        "}"
                );

        assertAbout(javaSource()).that(source)
                .processedWith(new FeatureProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedSource);
    }

    @Test public void checkOnEventWithAnnotatedParameters() throws Exception {

        JavaFileObject source = JavaFileObjects
                .forSourceLines("de.halfbit.featured.test.TestFeature",
                        "",
                        "package de.halfbit.featured.test;",
                        "import de.halfbit.featured.FeatureEvent;",
                        "import de.halfbit.featured.Feature;",
                        "import android.support.annotation.NonNull;",
                        "import android.support.annotation.Nullable;",
                        "public class TestFeature extends Feature<TestFeatureHost> {",
                        "    @FeatureEvent protected void onStart(@NonNull String event, @Nullable Object data) { }",
                        "}"
                );

        JavaFileObject expectedSource = JavaFileObjects
                .forSourceLines("de.halfbit.featured.test.TestFeatureHost",
                        "",
                        "package de.halfbit.featured.test;",
                        "import android.content.Context;",
                        "import android.support.annotation.NonNull;",
                        "import android.support.annotation.Nullable;",
                        "import de.halfbit.featured.FeatureHost;",
                        "public class TestFeatureHost extends FeatureHost<TestFeature, TestFeatureHost> {",
                        "    public TestFeatureHost(@NonNull Context context) {",
                        "        super(context);",
                        "    }",
                        "    public void dispatchOnStart(@NonNull String event, @Nullable Object data) {",
                        "        dispatch(new OnStartEvent(event, data));",
                        "    }",
                        "    private static final class OnStartEvent extends FeatureHost.Event<TestFeature> {",
                        "        private final @NonNull String mEvent;",
                        "        private final @Nullable Object mData;",
                        "        OnStartEvent(@NonNull String event, @Nullable Object data) {",
                        "            mEvent = event;",
                        "            mData = data;",
                        "        }",
                        "        @Override protected void dispatch(TestFeature feature) {",
                        "            feature.onStart(mEvent, mData);",
                        "        }",
                        "    }",
                        "}"
                );

        assertAbout(javaSource()).that(source)
                .processedWith(new FeatureProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedSource);
    }

    @Test public void checkOnEventErrorInheritFeature() throws Exception {

        JavaFileObject source = JavaFileObjects
                .forSourceLines("de.halfbit.featured.test.TestFeature",
                        "",
                        "package de.halfbit.featured.test;",
                        "import de.halfbit.featured.FeatureEvent;",
                        "import de.halfbit.featured.Feature;",
                        "public class TestFeature {",
                        "    @FeatureEvent protected void onStart() { }",
                        "}"
                );

        assertAbout(javaSource()).that(source)
                .processedWith(new FeatureProcessor())
                .failsToCompile()
                .withErrorContaining("must inherit from");
    }

    @Test public void checkOnEventErrorExpectReturnVoid() throws Exception {

        JavaFileObject source = JavaFileObjects
                .forSourceLines("de.halfbit.featured.test.TestFeature",
                        "",
                        "package de.halfbit.featured.test;",
                        "import de.halfbit.featured.FeatureEvent;",
                        "import de.halfbit.featured.Feature;",
                        "public class TestFeature extends Feature<TestFeatureHost> {",
                        "    @FeatureEvent protected boolean onStart() { return true; }",
                        "}"
                );

        assertAbout(javaSource()).that(source)
                .processedWith(new FeatureProcessor())
                .failsToCompile()
                .withErrorContaining("must return void");
    }

    @Test public void checkOnEventErrorExpectNotPrivate() throws Exception {

        JavaFileObject source = JavaFileObjects
                .forSourceLines("de.halfbit.featured.test.TestFeature",
                        "",
                        "package de.halfbit.featured.test;",
                        "import de.halfbit.featured.FeatureEvent;",
                        "import de.halfbit.featured.Feature;",
                        "public class TestFeature extends Feature<TestFeatureHost> {",
                        "    @FeatureEvent private void onStart() { }",
                        "}"
                );

        assertAbout(javaSource()).that(source)
                .processedWith(new FeatureProcessor())
                .failsToCompile()
                .withErrorContaining("must not be private or static");
    }

    @Test public void checkOnEventErrorExpectNotStatic() throws Exception {

        JavaFileObject source = JavaFileObjects
                .forSourceLines("de.halfbit.featured.test.TestFeature",
                        "",
                        "package de.halfbit.featured.test;",
                        "import de.halfbit.featured.FeatureEvent;",
                        "import de.halfbit.featured.Feature;",
                        "public class TestFeature extends Feature<TestFeatureHost> {",
                        "    @FeatureEvent static void onStart() { }",
                        "}"
                );

        assertAbout(javaSource()).that(source)
                .processedWith(new FeatureProcessor())
                .failsToCompile()
                .withErrorContaining("must not be private or static");
    }

    @Ignore // until feature inheritance is properly implemented
    @Test public void checkFeatureInheritance() throws Exception {

        JavaFileObject source = JavaFileObjects
                .forSourceLines("de.halfbit.featured.test.TestFeature",
                        "",
                        "package de.halfbit.featured.test;",
                        "import de.halfbit.featured.FeatureEvent;",
                        "import de.halfbit.featured.Feature;",
                        "",
                        "class StartFeature extends Feature<StartFeatureHost> {",
                        "    @FeatureEvent protected void onStart() { }",
                        "}",
                        "",
                        "class StopFeature extends StartFeature {",
                        "    @FeatureEvent protected void onStop() { }",
                        "}"
                );

        JavaFileObject expectedStartFeatureHost = JavaFileObjects
                .forSourceLines("de.halfbit.featured.test.StartFeatureHost",
                        "",
                        "package de.halfbit.featured.test;",
                        "import android.content.Context;",
                        "import android.support.annotation.NonNull;",
                        "import de.halfbit.featured.FeatureHost;",
                        "",
                        "public class StartFeatureHost<F extends StartFeature, FH extends StartFeatureHost> extends FeatureHost<F, FH> {",
                        "    public StartFeatureHost(@NonNull Context context) {",
                        "        super(context);",
                        "    }",
                        "    public void dispatchOnStart() {",
                        "        dispatch(new OnStartEvent());",
                        "    }",
                        "    private static final class OnStartEvent extends FeatureHost.Event<StartFeature> {",
                        "        @Override protected void dispatch(StartFeature feature) {",
                        "            feature.onStart();",
                        "        }",
                        "    }",
                        "}"
                );

        JavaFileObject expectedStopFeatureHost = JavaFileObjects
                .forSourceLines("de.halfbit.featured.test.StopFeatureHost",
                        "",
                        "package de.halfbit.featured.test;",
                        "import android.content.Context;",
                        "import android.support.annotation.NonNull;",
                        "import de.halfbit.featured.FeatureHost;",
                        "",
                        "public class StopFeatureHost extends StartFeatureHost<StopFeature, StopFeatureHost> {",
                        "    public StopFeatureHost(@NonNull Context context) {",
                        "        super(context);",
                        "    }",
                        "    public void dispatchOnStop() {",
                        "        dispatch(new OnStopEvent());",
                        "    }",
                        "    private static final class OnStopEvent extends FeatureHost.Event<StopFeature> {",
                        "        @Override protected void dispatch(StopFeature feature) {",
                        "            feature.onStop();",
                        "        }",
                        "    }",
                        "}"
                );

        assertAbout(javaSource()).that(source)
                .processedWith(new FeatureProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedStartFeatureHost, expectedStopFeatureHost);

    }

}
