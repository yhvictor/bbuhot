package(default_visibility = ["//visibility:public"])

load("@rules_proto//proto:defs.bzl", "proto_library")

proto_library(
    name = "config",
    srcs = glob(["*.proto"]),
)

java_proto_library(
    name = "config_java_proto",
    deps = [":config"],
)
