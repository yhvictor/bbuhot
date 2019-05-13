package(default_visibility = ["//visibility:public"])

java_library(
    name = "annotation",
    srcs = [
        "TestOnly.java",
    ],
    exported_plugins = [":checking_plugin"],
    exports = [
        "@maven//:com_google_code_findbugs_jsr305",
    ],
)

java_plugin(
    name = "checking_plugin",
    srcs = [
        "TestOnly.java",
        "TestOnlyCheck.java",
    ],
    deps = [
        "//third_party/java/auto_service",
        "@maven//:com_google_code_findbugs_jsr305",
        "@maven//:com_google_errorprone_error_prone_annotation",
        "@maven//:com_google_errorprone_error_prone_annotations",
        "@maven//:com_google_errorprone_error_prone_check_api",
        "@maven//:com_google_errorprone_error_prone_core",
        "@maven//:com_google_errorprone_javac",
    ],
)
