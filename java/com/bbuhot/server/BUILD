package(default_visibility = ["//java/com/bbuhot/server:server_group"])

package_group(
    name = "server_group",
    packages = [
        "//java/com/bbuhot/server/...",
        "//javatests/com/bbuhot/server/...",
    ],
)

load("@io_bazel_rules_docker//java:image.bzl", "java_image")
load("@io_bazel_rules_docker//container:container.bzl", "container_image", "container_push")

java_image(
    name = "server-bin",
    main_class = "com.bbuhot.server.app.EntryPoint",
    runtime_deps = [
        "//java/com/bbuhot/server/app",
        #"@maven//:org_hibernate_hibernate_c3p0",
    ],
)

container_push(
    name = "push_server",
    format = "Docker",
    image = ":server-bin",
    registry = "index.docker.io",
    repository = "yhvictor/bbuhot",
)
