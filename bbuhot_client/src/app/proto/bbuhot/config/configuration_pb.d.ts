// package: bhuhot.service
// file: bbuhot/config/configuration.proto

import * as jspb from "google-protobuf";

export class Configuration extends jspb.Message {
  hasDatabase(): boolean;
  clearDatabase(): void;
  getDatabase(): Configuration.Database;
  setDatabase(value?: Configuration.Database): void;

  hasDiscuzConfig(): boolean;
  clearDiscuzConfig(): void;
  getDiscuzConfig(): Configuration.DiscuzConfig;
  setDiscuzConfig(value?: Configuration.DiscuzConfig): void;

  hasHost(): boolean;
  clearHost(): void;
  getHost(): string | undefined;
  setHost(value: string): void;

  hasPort(): boolean;
  clearPort(): void;
  getPort(): number | undefined;
  setPort(value: number): void;

  hasIsDebug(): boolean;
  clearIsDebug(): void;
  getIsDebug(): boolean | undefined;
  setIsDebug(value: boolean): void;

  hasAllowCors(): boolean;
  clearAllowCors(): void;
  getAllowCors(): boolean | undefined;
  setAllowCors(value: boolean): void;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): Configuration.AsObject;
  static toObject(includeInstance: boolean, msg: Configuration): Configuration.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: Configuration, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): Configuration;
  static deserializeBinaryFromReader(message: Configuration, reader: jspb.BinaryReader): Configuration;
}

export namespace Configuration {
  export type AsObject = {
    database: Configuration.Database.AsObject,
    discuzConfig: Configuration.DiscuzConfig.AsObject,
    host?: string,
    port?: number,
    isDebug?: boolean,
    allowCors?: boolean,
  }

  export class Database extends jspb.Message {
    hasConnectionUrl(): boolean;
    clearConnectionUrl(): void;
    getConnectionUrl(): string | undefined;
    setConnectionUrl(value: string): void;

    hasUser(): boolean;
    clearUser(): void;
    getUser(): string | undefined;
    setUser(value: string): void;

    hasPassword(): boolean;
    clearPassword(): void;
    getPassword(): string | undefined;
    setPassword(value: string): void;

    hasTablePrefix(): boolean;
    clearTablePrefix(): void;
    getTablePrefix(): string | undefined;
    setTablePrefix(value: string): void;

    serializeBinary(): Uint8Array;
    toObject(includeInstance?: boolean): Database.AsObject;
    static toObject(includeInstance: boolean, msg: Database): Database.AsObject;
    static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
    static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
    static serializeBinaryToWriter(message: Database, writer: jspb.BinaryWriter): void;
    static deserializeBinary(bytes: Uint8Array): Database;
    static deserializeBinaryFromReader(message: Database, reader: jspb.BinaryReader): Database;
  }

  export namespace Database {
    export type AsObject = {
      connectionUrl?: string,
      user?: string,
      password?: string,
      tablePrefix?: string,
    }
  }

  export class DiscuzConfig extends jspb.Message {
    hasAuthKey(): boolean;
    clearAuthKey(): void;
    getAuthKey(): string | undefined;
    setAuthKey(value: string): void;

    clearAdminGroupList(): void;
    getAdminGroupList(): Array<number>;
    setAdminGroupList(value: Array<number>): void;
    addAdminGroup(value: number, index?: number): number;

    hasCookiePre(): boolean;
    clearCookiePre(): void;
    getCookiePre(): string | undefined;
    setCookiePre(value: string): void;

    serializeBinary(): Uint8Array;
    toObject(includeInstance?: boolean): DiscuzConfig.AsObject;
    static toObject(includeInstance: boolean, msg: DiscuzConfig): DiscuzConfig.AsObject;
    static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
    static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
    static serializeBinaryToWriter(message: DiscuzConfig, writer: jspb.BinaryWriter): void;
    static deserializeBinary(bytes: Uint8Array): DiscuzConfig;
    static deserializeBinaryFromReader(message: DiscuzConfig, reader: jspb.BinaryReader): DiscuzConfig;
  }

  export namespace DiscuzConfig {
    export type AsObject = {
      authKey?: string,
      adminGroupList: Array<number>,
      cookiePre?: string,
    }
  }
}

