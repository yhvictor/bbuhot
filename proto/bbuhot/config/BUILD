package(default_visibility = ["//visibility:public"])

proto_library(
    name = "config",
    srcs = glob(["*.proto"]),
)

java_proto_library(
    name = "config_java_proto",
    deps = [":config"],
)
