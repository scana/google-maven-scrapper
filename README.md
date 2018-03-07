# Google Maven artifacts scrapper

Simple Kotlin project for fetching list of all artifacts located in Google's Maven repository.

#### Usage:
```kotlin
val okHttpClient: OkHttpClient = ...
val scrapper = Scrapper(okHttpClient)
val results = scrapper.scrap()
```

#### Results:
```
...
com.android.support.constraint:constraint-layout-solver
com.android.support.constraint:constraint-layout
com.android.databinding:library
com.android.databinding:adapters
com.android.databinding:compiler
com.android.databinding:compilerCommon
com.android.databinding:baseLibrary
com.android.support:support-compat
com.android.support:leanback-v17
com.android.support:recommendation
com.android.support:support-tv-provider
com.android.support:support-vector-drawable
com.android.support:recyclerview-v7
com.android.support:preference-leanback-v17
com.android.support:preference-v14
...
```

### Dependencies:
- OkHttp3
- Okio
