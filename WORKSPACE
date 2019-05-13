load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")

# Skylary
http_archive(
    name = "bazel_skylib",
    strip_prefix = "bazel-skylib-0.8.0",
    urls = ["https://github.com/bazelbuild/bazel-skylib/archive/0.8.0.tar.gz"],
)

load("@bazel_skylib//lib:versions.bzl", "versions")

versions.check(minimum_bazel_version = "0.5.4")

# Protobuf
http_archive(
    name = "com_google_protobuf",
    strip_prefix = "protobuf-3.8.0-rc1",
    urls = ["https://github.com/protocolbuffers/protobuf/archive/v3.8.0-rc1.zip"],
)

load("@com_google_protobuf//:protobuf_deps.bzl", "protobuf_deps")

protobuf_deps()

# Maven
RULES_JVM_EXTERNAL_TAG = "2.0.1"

http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        "com.google.auto:auto-common:0.10",
        "com.google.auto.service:auto-service:1.0-rc5",
        "com.google.code.findbugs:jsr305:3.0.2",
        "com.google.code.gson:gson:2.8.5",
        "com.google.dagger:dagger:2.22.1",
        "com.google.dagger:dagger-compiler:2.22.1",
        "com.google.errorprone:error_prone_core:2.3.2",
        "com.google.guava:guava:27.1-jre",
        "com.h2database:h2:1.4.197",
        "io.undertow:undertow-core:2.0.1.Final",
        "junit:junit:4.12",
        "mysql:mysql-connector-java:8.0.13",
        "org.flywaydb:flyway-core:5.2.4",
        "org.hibernate:hibernate-c3p0:5.4.2.Final",
        "org.hibernate:hibernate-core:5.4.2.Final",
        "org.hibernate.javax.persistence:hibernate-jpa-2.1-api:1.0.0.Final",
    ],
    repositories = [
        "https://repo.maven.apache.org/maven2",
        "https://maven.google.com",
    ],
)

bind(
    name = "error_prone_annotations",
    actual = "@maven//:com_google_errorprone_error_prone_annotations",
)

bind(
    name = "gson",
    actual = "@maven//:com_google_code_gson_gson",
)

bind(
    name = "guava",
    actual = "@maven//:com_google_guava_guava",
)

# Docker
http_archive(
    name = "io_bazel_rules_docker",
    strip_prefix = "rules_docker-0.7.0",
    urls = ["https://github.com/bazelbuild/rules_docker/archive/v0.7.0.tar.gz"],
)

load(
    "@io_bazel_rules_docker//java:image.bzl",
    _java_image_repos = "repositories",
)

_java_image_repos()

# Go
http_archive(
    name = "io_bazel_rules_go",
    sha256 = "3743a20704efc319070957c45e24ae4626a05ba4b1d6a8961e87520296f1b676",
    urls = ["https://github.com/bazelbuild/rules_go/releases/download/0.18.4/rules_go-0.18.4.tar.gz"],
)

load("@io_bazel_rules_go//go:deps.bzl", "go_register_toolchains", "go_rules_dependencies")

go_rules_dependencies()

go_register_toolchains()

# Node JS
http_archive(
    name = "build_bazel_rules_nodejs",
    urls = ["https://github.com/bazelbuild/rules_nodejs/releases/download/0.29.0/rules_nodejs-0.29.0.tar.gz"],
)

load("@build_bazel_rules_nodejs//:defs.bzl", "node_repositories", "yarn_install")

node_repositories(
    package_json = ["//third_party/node_js:package.json"],
)

yarn_install(
    name = "npm",
    package_json = "//third_party/node_js:package.json",
    yarn_lock = "//third_party/node_js:yarn.lock",
)

load("@npm//:install_bazel_dependencies.bzl", "install_bazel_dependencies")

install_bazel_dependencies()

load("@npm_bazel_karma//:package.bzl", "rules_karma_dependencies")

rules_karma_dependencies()

load("@io_bazel_rules_webtesting//web:repositories.bzl", "web_test_repositories")

web_test_repositories()

load("@npm_bazel_karma//:browser_repositories.bzl", "browser_repositories")

browser_repositories()

load("@npm_bazel_typescript//:defs.bzl", "ts_setup_workspace")

ts_setup_workspace()
