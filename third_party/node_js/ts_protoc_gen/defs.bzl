TypescriptProtoLibraryAspect = provider(
    fields = {
        "js_outputs": "The JS output files produced directly from the src protos",
        "dts_outputs": "Ths TS definition files produced directly from the src protos",
        "deps_js": "The transitive JS dependencies",
        "deps_dts": "The transitive dependencies' TS definitions",
    },
)

def proto_path(proto):
    """
    The proto path is not really a file path
    It's the path to the proto that was seen when the descriptor file was generated.
    """
    path = proto.path
    root = proto.root.path
    ws = proto.owner.workspace_root
    if path.startswith(root):
        path = path[len(root):]
    if path.startswith("/"):
        path = path[1:]
    if path.startswith(ws):
        path = path[len(ws):]
    if path.startswith("/"):
        path = path[1:]
    return path

def append_to_outputs(ctx, file_name, js_outputs, dts_outputs):
    generated_filenames = ["_pb.d.ts", "_pb.js"]

    for f in generated_filenames:
        output = ctx.actions.declare_file(file_name + f)
        if f.endswith(".d.ts"):
            dts_outputs.append(output)
        else:
            js_outputs.append(output)

def typescript_proto_library_aspect_(target, ctx):
    """
    A bazel aspect that is applied on every proto_library rule on the transitive set of dependencies
    of a typescript_proto_library rule.

    Handles running protoc to produce the generated JS and TS files.
    """
    js_outputs = []
    dts_outputs = []
    proto_inputs = []

    inputs = depset([ctx.file._protoc])
    for src in target.proto.direct_sources:
        if src.extension != "proto":
            fail("Input must be a proto file")

        file_name = src.basename[:-len(src.extension) - 1]
        normalized_file = proto_path(src)
        proto_inputs.append(normalized_file)
        append_to_outputs(ctx, file_name, js_outputs, dts_outputs)

    outputs = dts_outputs + js_outputs

    inputs += ctx.files._ts_protoc_gen
    inputs += target.proto.direct_sources
    inputs += target.proto.transitive_descriptor_sets

    descriptor_sets_paths = [desc.path for desc in target.proto.transitive_descriptor_sets]

    protoc_output_dir = ctx.var["BINDIR"]
    protoc_command = "%s" % (ctx.file._protoc.path)

    protoc_command += " --plugin=protoc-gen-ts=%s" % (ctx.files._ts_protoc_gen[1].path)
    protoc_command += " --ts_out=service=true:%s" % (protoc_output_dir)
    protoc_command += " --js_out=import_style=commonjs,binary:%s" % (protoc_output_dir)
    protoc_command += " --descriptor_set_in=%s" % (":".join(descriptor_sets_paths))
    protoc_command += " %s" % (" ".join(proto_inputs))

    commands = [protoc_command]
    command = " && ".join(commands)
    ctx.actions.run_shell(
        inputs = inputs,
        outputs = outputs,
        progress_message = "Creating Typescript pb files %s" % ctx.label,
        command = command,
    )

    dts_outputs = depset(dts_outputs)
    js_outputs = depset(js_outputs)
    deps_js = depset([])
    deps_dts = depset([])

    for dep in ctx.rule.attr.deps:
        aspect_data = dep[TypescriptProtoLibraryAspect]
        deps_dts += aspect_data.dts_outputs + aspect_data.deps_dts
        deps_js += aspect_data.js_outputs + aspect_data.deps_js

    return [TypescriptProtoLibraryAspect(
        dts_outputs = dts_outputs,
        js_outputs = js_outputs,
        deps_dts = deps_dts,
        deps_js = deps_js,
    )]

typescript_proto_library_aspect = aspect(
    implementation = typescript_proto_library_aspect_,
    attr_aspects = ["deps"],
    attrs = {
        "_ts_protoc_gen": attr.label(
            allow_files = True,
            executable = True,
            cfg = "host",
            default = Label("//third_party/node_js/ts_protoc_gen"),
        ),
        "_protoc": attr.label(
            allow_files = True,
            single_file = True,
            executable = True,
            cfg = "host",
            default = Label("@com_google_protobuf//:protoc"),
        ),
    },
)

def _typescript_proto_library_impl(ctx):
    """
    Handles converting the aspect output into a provider compatible with the rules_typescript rules.
    """
    aspect_data = ctx.attr.proto[TypescriptProtoLibraryAspect]
    dts_outputs = aspect_data.dts_outputs
    js_outputs = aspect_data.js_outputs
    outputs = js_outputs + dts_outputs

    return struct(
        typescript = struct(
            declarations = dts_outputs,
            transitive_declarations = dts_outputs + aspect_data.deps_dts,
            type_blacklisted_declarations = depset([]),
            es5_sources = js_outputs + aspect_data.deps_js,
            es6_sources = js_outputs + aspect_data.deps_js,
            transitive_es5_sources = js_outputs + aspect_data.deps_js,
            transitive_es6_sources = js_outputs + aspect_data.deps_js,
        ),
        legacy_info = struct(
            files = outputs,
        ),
        providers = [
            DefaultInfo(files = outputs),
        ],
    )

typescript_proto_library = rule(
    attrs = {
        "proto": attr.label(
            mandatory = True,
            allow_files = True,
            single_file = True,
            providers = ["proto"],
            aspects = [typescript_proto_library_aspect],
        ),
        "_ts_protoc_gen": attr.label(
            allow_files = True,
            executable = True,
            cfg = "host",
            default = Label("//third_party/node_js/ts_protoc_gen"),
        ),
        "_protoc": attr.label(
            allow_files = True,
            single_file = True,
            executable = True,
            cfg = "host",
            default = Label("@com_google_protobuf//:protoc"),
        ),
    },
    implementation = _typescript_proto_library_impl,
)
