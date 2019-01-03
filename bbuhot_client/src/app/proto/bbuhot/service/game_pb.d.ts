// package: bhuhot.service
// file: bbuhot/service/game.proto

import * as jspb from "google-protobuf";
import * as bbuhot_service_auth_pb from "../../bbuhot/service/auth_pb";

export class Game extends jspb.Message {
  hasId(): boolean;
  clearId(): void;
  getId(): number | undefined;
  setId(value: number): void;

  hasName(): boolean;
  clearName(): void;
  getName(): string | undefined;
  setName(value: string): void;

  hasDescription(): boolean;
  clearDescription(): void;
  getDescription(): string | undefined;
  setDescription(value: string): void;

  hasNormalUserVisible(): boolean;
  clearNormalUserVisible(): void;
  getNormalUserVisible(): boolean | undefined;
  setNormalUserVisible(value: boolean): void;

  hasStatus(): boolean;
  clearStatus(): void;
  getStatus(): Game.Status | undefined;
  setStatus(value: Game.Status): void;

  hasBetOptionLimit(): boolean;
  clearBetOptionLimit(): void;
  getBetOptionLimit(): number | undefined;
  setBetOptionLimit(value: number): void;

  hasBetAmountLowest(): boolean;
  clearBetAmountLowest(): void;
  getBetAmountLowest(): number | undefined;
  setBetAmountLowest(value: number): void;

  hasBetAmountHighest(): boolean;
  clearBetAmountHighest(): void;
  getBetAmountHighest(): number | undefined;
  setBetAmountHighest(value: number): void;

  hasEndTimeMs(): boolean;
  clearEndTimeMs(): void;
  getEndTimeMs(): number | undefined;
  setEndTimeMs(value: number): void;

  clearBettingOptionsList(): void;
  getBettingOptionsList(): Array<Game.BettingOption>;
  setBettingOptionsList(value: Array<Game.BettingOption>): void;
  addBettingOptions(value?: Game.BettingOption, index?: number): Game.BettingOption;

  hasWinningOption(): boolean;
  clearWinningOption(): void;
  getWinningOption(): number | undefined;
  setWinningOption(value: number): void;

  clearBetsList(): void;
  getBetsList(): Array<Game.Bet>;
  setBetsList(value: Array<Game.Bet>): void;
  addBets(value?: Game.Bet, index?: number): Game.Bet;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): Game.AsObject;
  static toObject(includeInstance: boolean, msg: Game): Game.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: Game, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): Game;
  static deserializeBinaryFromReader(message: Game, reader: jspb.BinaryReader): Game;
}

export namespace Game {
  export type AsObject = {
    id?: number,
    name?: string,
    description?: string,
    normalUserVisible?: boolean,
    status?: Game.Status,
    betOptionLimit?: number,
    betAmountLowest?: number,
    betAmountHighest?: number,
    endTimeMs?: number,
    bettingOptionsList: Array<Game.BettingOption.AsObject>,
    winningOption?: number,
    betsList: Array<Game.Bet.AsObject>,
  }

  export class BettingOption extends jspb.Message {
    hasName(): boolean;
    clearName(): void;
    getName(): string | undefined;
    setName(value: string): void;

    hasOdds(): boolean;
    clearOdds(): void;
    getOdds(): number | undefined;
    setOdds(value: number): void;

    serializeBinary(): Uint8Array;
    toObject(includeInstance?: boolean): BettingOption.AsObject;
    static toObject(includeInstance: boolean, msg: BettingOption): BettingOption.AsObject;
    static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
    static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
    static serializeBinaryToWriter(message: BettingOption, writer: jspb.BinaryWriter): void;
    static deserializeBinary(bytes: Uint8Array): BettingOption;
    static deserializeBinaryFromReader(message: BettingOption, reader: jspb.BinaryReader): BettingOption;
  }

  export namespace BettingOption {
    export type AsObject = {
      name?: string,
      odds?: number,
    }
  }

  export class Bet extends jspb.Message {
    hasBettingOptionId(): boolean;
    clearBettingOptionId(): void;
    getBettingOptionId(): number | undefined;
    setBettingOptionId(value: number): void;

    hasMoney(): boolean;
    clearMoney(): void;
    getMoney(): number | undefined;
    setMoney(value: number): void;

    serializeBinary(): Uint8Array;
    toObject(includeInstance?: boolean): Bet.AsObject;
    static toObject(includeInstance: boolean, msg: Bet): Bet.AsObject;
    static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
    static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
    static serializeBinaryToWriter(message: Bet, writer: jspb.BinaryWriter): void;
    static deserializeBinary(bytes: Uint8Array): Bet;
    static deserializeBinaryFromReader(message: Bet, reader: jspb.BinaryReader): Bet;
  }

  export namespace Bet {
    export type AsObject = {
      bettingOptionId?: number,
      money?: number,
    }
  }

  export enum Status {
    DRAFT = 0,
    PUBLISHED = 1,
    SETTLED = 2,
    CANCELLED = 3,
  }
}

export class AdminGameRequest extends jspb.Message {
  hasAuth(): boolean;
  clearAuth(): void;
  getAuth(): bbuhot_service_auth_pb.AuthRequest;
  setAuth(value?: bbuhot_service_auth_pb.AuthRequest): void;

  hasGame(): boolean;
  clearGame(): void;
  getGame(): Game;
  setGame(value?: Game): void;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): AdminGameRequest.AsObject;
  static toObject(includeInstance: boolean, msg: AdminGameRequest): AdminGameRequest.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: AdminGameRequest, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): AdminGameRequest;
  static deserializeBinaryFromReader(message: AdminGameRequest, reader: jspb.BinaryReader): AdminGameRequest;
}

export namespace AdminGameRequest {
  export type AsObject = {
    auth: bbuhot_service_auth_pb.AuthRequest.AsObject,
    game: Game.AsObject,
  }
}

export class AdminGameReply extends jspb.Message {
  hasAuthErrorCode(): boolean;
  clearAuthErrorCode(): void;
  getAuthErrorCode(): bbuhot_service_auth_pb.AuthReply.AuthErrorCode | undefined;
  setAuthErrorCode(value: bbuhot_service_auth_pb.AuthReply.AuthErrorCode): void;

  hasGame(): boolean;
  clearGame(): void;
  getGame(): Game | undefined;
  setGame(value?: Game): void;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): AdminGameReply.AsObject;
  static toObject(includeInstance: boolean, msg: AdminGameReply): AdminGameReply.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: AdminGameReply, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): AdminGameReply;
  static deserializeBinaryFromReader(message: AdminGameReply, reader: jspb.BinaryReader): AdminGameReply;
}

export namespace AdminGameReply {
  export type AsObject = {
    authErrorCode?: bbuhot_service_auth_pb.AuthReply.AuthErrorCode,
    game?: Game.AsObject,
  }
}

export class AdminGameStatusRequest extends jspb.Message {
  hasAuth(): boolean;
  clearAuth(): void;
  getAuth(): bbuhot_service_auth_pb.AuthRequest;
  setAuth(value?: bbuhot_service_auth_pb.AuthRequest): void;

  hasGameId(): boolean;
  clearGameId(): void;
  getGameId(): number | undefined;
  setGameId(value: number): void;

  hasGameStatus(): boolean;
  clearGameStatus(): void;
  getGameStatus(): Game.Status | undefined;
  setGameStatus(value: Game.Status): void;

  hasWinningOption(): boolean;
  clearWinningOption(): void;
  getWinningOption(): number | undefined;
  setWinningOption(value: number): void;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): AdminGameStatusRequest.AsObject;
  static toObject(includeInstance: boolean, msg: AdminGameStatusRequest): AdminGameStatusRequest.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: AdminGameStatusRequest, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): AdminGameStatusRequest;
  static deserializeBinaryFromReader(message: AdminGameStatusRequest, reader: jspb.BinaryReader): AdminGameStatusRequest;
}

export namespace AdminGameStatusRequest {
  export type AsObject = {
    auth: bbuhot_service_auth_pb.AuthRequest.AsObject,
    gameId?: number,
    gameStatus?: Game.Status,
    winningOption?: number,
  }
}

export class AdminGameStatusReply extends jspb.Message {
  hasAuthErrorCode(): boolean;
  clearAuthErrorCode(): void;
  getAuthErrorCode(): bbuhot_service_auth_pb.AuthReply.AuthErrorCode | undefined;
  setAuthErrorCode(value: bbuhot_service_auth_pb.AuthReply.AuthErrorCode): void;

  hasGameStatusErrorCode(): boolean;
  clearGameStatusErrorCode(): void;
  getGameStatusErrorCode(): AdminGameStatusReply.ErrorCode | undefined;
  setGameStatusErrorCode(value: AdminGameStatusReply.ErrorCode): void;

  hasGame(): boolean;
  clearGame(): void;
  getGame(): Game | undefined;
  setGame(value?: Game): void;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): AdminGameStatusReply.AsObject;
  static toObject(includeInstance: boolean, msg: AdminGameStatusReply): AdminGameStatusReply.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: AdminGameStatusReply, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): AdminGameStatusReply;
  static deserializeBinaryFromReader(message: AdminGameStatusReply, reader: jspb.BinaryReader): AdminGameStatusReply;
}

export namespace AdminGameStatusReply {
  export type AsObject = {
    authErrorCode?: bbuhot_service_auth_pb.AuthReply.AuthErrorCode,
    gameStatusErrorCode?: AdminGameStatusReply.ErrorCode,
    game?: Game.AsObject,
  }

  export enum ErrorCode {
    NO_ERROR = 0,
    LOCKED = 1,
  }
}

export class ListGameRequest extends jspb.Message {
  hasAuth(): boolean;
  clearAuth(): void;
  getAuth(): bbuhot_service_auth_pb.AuthRequest;
  setAuth(value?: bbuhot_service_auth_pb.AuthRequest): void;

  hasIsAdminRequest(): boolean;
  clearIsAdminRequest(): void;
  getIsAdminRequest(): boolean | undefined;
  setIsAdminRequest(value: boolean): void;

  hasGameStatus(): boolean;
  clearGameStatus(): void;
  getGameStatus(): Game.Status | undefined;
  setGameStatus(value: Game.Status): void;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): ListGameRequest.AsObject;
  static toObject(includeInstance: boolean, msg: ListGameRequest): ListGameRequest.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: ListGameRequest, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): ListGameRequest;
  static deserializeBinaryFromReader(message: ListGameRequest, reader: jspb.BinaryReader): ListGameRequest;
}

export namespace ListGameRequest {
  export type AsObject = {
    auth: bbuhot_service_auth_pb.AuthRequest.AsObject,
    isAdminRequest?: boolean,
    gameStatus?: Game.Status,
  }
}

export class ListGameReply extends jspb.Message {
  hasAuthErrorCode(): boolean;
  clearAuthErrorCode(): void;
  getAuthErrorCode(): bbuhot_service_auth_pb.AuthReply.AuthErrorCode | undefined;
  setAuthErrorCode(value: bbuhot_service_auth_pb.AuthReply.AuthErrorCode): void;

  clearGamesList(): void;
  getGamesList(): Array<Game>;
  setGamesList(value: Array<Game>): void;
  addGames(value?: Game, index?: number): Game;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): ListGameReply.AsObject;
  static toObject(includeInstance: boolean, msg: ListGameReply): ListGameReply.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: ListGameReply, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): ListGameReply;
  static deserializeBinaryFromReader(message: ListGameReply, reader: jspb.BinaryReader): ListGameReply;
}

export namespace ListGameReply {
  export type AsObject = {
    authErrorCode?: bbuhot_service_auth_pb.AuthReply.AuthErrorCode,
    gamesList: Array<Game.AsObject>,
  }
}

export class BetRequest extends jspb.Message {
  hasAuth(): boolean;
  clearAuth(): void;
  getAuth(): bbuhot_service_auth_pb.AuthRequest;
  setAuth(value?: bbuhot_service_auth_pb.AuthRequest): void;

  hasGameId(): boolean;
  clearGameId(): void;
  getGameId(): number | undefined;
  setGameId(value: number): void;

  clearBetsList(): void;
  getBetsList(): Array<Game.Bet>;
  setBetsList(value: Array<Game.Bet>): void;
  addBets(value?: Game.Bet, index?: number): Game.Bet;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): BetRequest.AsObject;
  static toObject(includeInstance: boolean, msg: BetRequest): BetRequest.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: BetRequest, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): BetRequest;
  static deserializeBinaryFromReader(message: BetRequest, reader: jspb.BinaryReader): BetRequest;
}

export namespace BetRequest {
  export type AsObject = {
    auth: bbuhot_service_auth_pb.AuthRequest.AsObject,
    gameId?: number,
    betsList: Array<Game.Bet.AsObject>,
  }
}

export class BetReply extends jspb.Message {
  hasAuthErrorCode(): boolean;
  clearAuthErrorCode(): void;
  getAuthErrorCode(): bbuhot_service_auth_pb.AuthReply.AuthErrorCode | undefined;
  setAuthErrorCode(value: bbuhot_service_auth_pb.AuthReply.AuthErrorCode): void;

  hasBetErrorCode(): boolean;
  clearBetErrorCode(): void;
  getBetErrorCode(): BetReply.BetErrorCode | undefined;
  setBetErrorCode(value: BetReply.BetErrorCode): void;

  clearBetsList(): void;
  getBetsList(): Array<Game.Bet>;
  setBetsList(value: Array<Game.Bet>): void;
  addBets(value?: Game.Bet, index?: number): Game.Bet;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): BetReply.AsObject;
  static toObject(includeInstance: boolean, msg: BetReply): BetReply.AsObject;
  static extensions: {[key: number]: jspb.ExtensionFieldInfo<jspb.Message>};
  static extensionsBinary: {[key: number]: jspb.ExtensionFieldBinaryInfo<jspb.Message>};
  static serializeBinaryToWriter(message: BetReply, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): BetReply;
  static deserializeBinaryFromReader(message: BetReply, reader: jspb.BinaryReader): BetReply;
}

export namespace BetReply {
  export type AsObject = {
    authErrorCode?: bbuhot_service_auth_pb.AuthReply.AuthErrorCode,
    betErrorCode?: BetReply.BetErrorCode,
    betsList: Array<Game.Bet.AsObject>,
  }

  export enum BetErrorCode {
    NO_ERROR = 0,
    MONEY_TOO_LOW = 1,
    MONEY_TOO_HIGH = 2,
    OPTION_TOO_MANY = 3,
    NO_ENOUGH_MONEY = 4,
  }
}

