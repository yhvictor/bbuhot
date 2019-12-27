load("@rules_proto//proto:defs.bzl", "ProtoInfo")

TypescriptProtoLibraryAspect = provider(
    fields = {
        "js_outputs": "The JS output files produced directly from the src protos",
        "dts_outputs": "Ths TS definition files produced directly from the src protos",
        "deps_js": "The transitive JS dependencies",
        "deps_dts": "The transitive dependencies' TS definitions",
    },
)

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
    direct_sources_path = []

    for src in target[ProtoInfo].direct_sources:
        if src.extension != "proto":
            fail("Input must be a proto file")

        file_name = src.basename[:-len(src.extension) - 1]
        direct_sources_path.append(src.path)
        append_to_outputs(ctx, file_name, js_outputs, dts_outputs)

    outputs = dts_outputs + js_outputs
    descriptor_sets = target[ProtoInfo].transitive_descriptor_sets.to_list();
    protoc_output_dir = ctx.var["BINDIR"]

    tools = ctx.files._protoc + ctx.files._ts_protoc_gen
    
    protoc_command = "%s" % (ctx.executable._protoc.path)
    protoc_command += " --plugin=protoc-gen-ts=%s" % (ctx.executable._ts_protoc_gen.path)
    protoc_command += " --ts_out=service=true:%s" % (protoc_output_dir)
    protoc_command += " --js_out=import_style=commonjs,binary:%s" % (protoc_output_dir)
    protoc_command += " --descriptor_set_in=%s" % (":".join([desc.path for desc in descriptor_sets]))
    protoc_command += " %s" % (" ".join(direct_sources_path))

    ctx.actions.run_shell(
        inputs = descriptor_sets,
        outputs = outputs,
        tools = tools,
        progress_message = "Creating Typescript pb files %s" % ctx.label,
        command = protoc_command,
    )

    deps_dts = []
    deps_js = []

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
            default = Label("//third_party/node_js/ts_protoc_gen"),
            cfg = "host",
            allow_files = True,
            executable = True,
        ),
        "_protoc": attr.label(
            default = Label("@com_google_protobuf//:protoc"),
            cfg = "host",
            allow_single_file = True,
            executable = True,
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
            DefaultInfo(files = depset(outputs)),
        ],
    )

typescript_proto_library = rule(
    attrs = {
        "proto": attr.label(
            mandatory = True,
            allow_single_file = True,
            providers = [ProtoInfo],
            aspects = [typescript_proto_library_aspect],
        ),
    },
    implementation = _typescript_proto_library_impl,
)
