<img src="/images/tags.png" width="467" height="260">

# Flow layout

Supports default android views and **new android compose**

# FlowLayout

![Min Android Sdk](https://img.shields.io/badge/minSdkVersion-16-212121.svg)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.st235/flow-layout/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.st235/flow-layout)

Small library to support flexbox flow aligned views composition

## Download it from

__Important: library was migrated from JCenter to MavenCentral__ 

It means that it may be necessary to add __mavenCentral__ repository to your repositories list

```groovy
allprojects {
    repositories {
        // your repositories

        mavenCentral()
    }
}
```

- Maven

```
<dependency>
  <groupId>com.github.st235</groupId>
  <artifactId>flow-layout</artifactId>
  <version>X.X.X</version>
  <type>pom</type>
</dependency>
```

- Gradle

```
implementation 'com.github.st235:flow-layout:X.X.X'
```

- Ivy

```
<dependency org='com.github.st235' name='flow-layout' rev='X.X.X'>
  <artifact name='flow-layout' ext='pom' ></artifact>
</dependency>
```

## Usage

```xml

    <st235.com.github.flowlayout.FlowLayout
        android:id="@+id/flowLayout"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent">
        
        ... child views goes here
        
    </st235.com.github.flowlayout.FlowLayout>

```

## Xml attributes

| property | type | description |
| ----- | ----- | ----- |
| **fl_gravity** | enum | possible values is: left, right, start, end, center, justify |

# FlowLayout Compose

![Min Android Sdk](https://img.shields.io/badge/minSdkVersion-21-212121.svg)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.st235/flow-layout-compose/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.st235/flow-layout-compose)

## Download

__Important: library was migrated from JCenter to MavenCentral__

It means that it may be necessary to add __mavenCentral__ repository to your repositories list

```groovy
allprojects {
    repositories {
        // your repositories

        mavenCentral()
    }
}
```

- Maven

```
<dependency>
  <groupId>com.github.st235</groupId>
  <artifactId>flow-layout-compose</artifactId>
  <version>X.X.X</version>
  <type>pom</type>
</dependency>
```

- Gradle

```
implementation 'com.github.st235:flow-layout-compose:X.X.X'
```

- Ivy

```
<dependency org='com.github.st235' name='flow-layout-compose' rev='X.X.X'>
  <artifact name='flow-layout' ext='pom' ></artifact>
</dependency>
```

## Usage

```kotlin
val scroll = rememberScrollState()

FlowLayout(
    direction = FlowLayoutDirection.START,
    modifier = Modifier
        .background(Color.White)
        .verticalScroll(scroll)
        .fillMaxWidth()
) {
    for (catName in stringArrayResource(id = R.array.cats_tags)) {
        Text(
            text = catName,
            modifier = Modifier
                .padding(8.dp, 4.dp)
                .background(colorResource(id = R.color.colorGreyOp80), RoundedCornerShape(4.dp))
                .padding(8.dp, 4.dp),
            color = Color.White,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.padding(2.dp))
    }
}
```

# Screens

| Left | Right |
| ------------- | ------------- |
| <img src="/images/left.png" width="250" height="150"> | <img src="/images/right.png" width="250" height="150">

| Center | Justify |
| ------------- | ------------- |
| <img src="/images/center.png" width="250" height="150"> | <img src="/images/justify.png" width="250" height="150"> |



# License

```
MIT License

Copyright (c) 2020 Alexander Dadukin

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
