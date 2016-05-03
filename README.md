# ViewHolderBinder

## Description

ViewHolderBinder makes it easy to manage multiple ViewHolders.

## Quick Start

Check out the included example project to see everything in action.

### Step 1: Create a ViewHolder.

```java
public class FilmViewHolder extends RecyclerView.ViewHolder {
    public FilmViewHolder(View itemView) {
        super(itemView);
    }

    public void render(Filmography filmography) {
        ((TextView) itemView).setText("FILM: " + filmography.title);
    }
}
```

### Step 2: Define the use to ViewHolder.

```java
public class FilmographyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ...
    private final FilmographyAdapterViewHolderBinder binder;

    public FilmographyAdapter(Context context) {
        ...
        // The name of the ViewHolderBinder will be
        // the "Class name(ex. FilmographyAdapter)" + "ViewHolderBinder".
        this.binder = new FilmographyAdapterViewHolderBinder(context, this);
    }

    ...

    // Add @ViewHolder annotation to view-holder parameter.
    void onBindViewHolder(@ViewHolder(viewType = 1, layout = R.layout.your_layout) FilmViewHolder holder, int position) {
        holder.render(items.get(position));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Select the FilmViewHolder from ViewType and create ViewHolder instance.
        return binder.create(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Select the FilmViewHolder from ViewType
        // and run onBindViewHolder(FilmViewHolder, int) method.
        binder.bind(holder, position);
    }

    ...

    @Override
    public int getItemViewType(int position) {
        return 1; // You should be implemented.
    }
}
```

### Step 3: Finish!

:tada::tada::tada:

## Install

### Gradle

#### Step 1

Add it in your root build.gradle at the end of repositories:

```
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

#### Step 2

Add the dependency

```
dependencies {
    compile 'com.github.hisaichi5518:view-holder-binder:v0.0.1'
}
```

### Maven

#### Step 1

Add the JitPack repository to your build file

```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

#### Step 2

Add the dependency

```
<dependency>
    <groupId>com.github.hisaichi5518</groupId>
    <artifactId>view-holder-binder</artifactId>
    <version>v0.0.1</version>
</dependency>
```

## See Also

[Recycler-Binder](https://github.com/satorufujiwara/recyclerview-binder)

## Author

[@hisaichi5518](https://twitter.com/hisaichi5518)

## LICENSE

```
The MIT License (MIT)

Copyright (c) 2016 hisaichi5518

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
