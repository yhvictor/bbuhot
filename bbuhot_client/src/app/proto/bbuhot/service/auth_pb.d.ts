// package: bhuhot.service
// file: bbuhot/service/auth.proto

import * as jspb from "google-protobuf";

export class AuthRequest extends jspb.Message {
  hasAuth(): boolean;
  clearAuth(): void;
  getAuth(): string | undefined;
  setAuth(value: string): void;

  hasSaltKey(): boolean;
  clearSaltKey(): void;
  getSaltKey(): string | undefined;
  setSaltKey(value: string): void;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): AuthRequest.AsObject;
  static toObject(includeInstance: boolean, msg: AuthRequest): AuthRequest.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: AuthRequest, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): AuthRequest;
  static deserializeBinaryFromReader(message: AuthRequest, reader: jspb.BinaryReader): AuthRequest;
}

export namespace AuthRequest {
  export type AsObject = {
    auth?: string,
    saltKey?: string,
  }
}

export class AuthReply extends jspb.Message {
  hasErrorCode(): boolean;
  clearErrorCode(): void;
  getErrorCode(): AuthReply.AuthErrorCode | undefined;
  setErrorCode(value: AuthReply.AuthErrorCode): void;

  hasUser(): boolean;
  clearUser(): void;
  getUser(): AuthReply.User | undefined;
  setUser(value?: AuthReply.User): void;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): AuthReply.AsObject;
  static toObject(includeInstance: boolean, msg: AuthReply): AuthReply.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: AuthReply, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): AuthReply;
  static deserializeBinaryFromReader(message: AuthReply, reader: jspb.BinaryReader): AuthReply;
}

export namespace AuthReply {
  export type AsObject = {
    errorCode?: AuthReply.AuthErrorCode,
    user?: AuthReply.User.AsObject,
  }

  export class User extends jspb.Message {
    hasUid(): boolean;
    clearUid(): void;
    getUid(): number | undefined;
    setUid(value: number): void;

    hasName(): boolean;
    clearName(): void;
    getName(): string | undefined;
    setName(value: string): void;

    serializeBinary(): Uint8Array;
    toObject(includeInstance?: boolean): User.AsObject;
    static toObject(includeInstance: boolean, msg: User): User.AsObject;
    static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
    static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
    static serializeBinaryToWriter(message: User, writer: jspb.BinaryWriter): void;
    static deserializeBinary(bytes: Uint8Array): User;
    static deserializeBinaryFromReader(message: User, reader: jspb.BinaryReader): User;
  }

  export namespace User {
    export type AsObject = {
      uid?: number,
      name?: string,
    }
  }

  export enum AuthErrorCode {
    NO_ERROR = 0,
    KEY_NOT_MATCHING = 1,
    PERMISSION_DENY = 2,
  }
}

