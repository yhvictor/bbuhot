import { Injectable } from '@angular/core';
import { ApiMethods } from '../api/api-methods';
import { Game } from '../proto/bbuhot/service/game_pb';

export class GameAccessor {
  public id: number;
  public name: string;
  public endTime: Date;
  public isSimpleGame: boolean;
  public firstBetOption: Game.BettingOption.AsObject;
  public secondBetOption: Game.BettingOption.AsObject;

  constructor(public game: Game) {
    this.id = game.getId() as number;
    this.name = game.getName() as string;
    this.endTime = new Date(game.getEndTimeMs() as number);
    this.isSimpleGame = game.getBettingOptionsList().length === 2;
    if (this.isSimpleGame) {
      this.firstBetOption = game.getBettingOptionsList()[0].toObject();
      this.secondBetOption = game.getBettingOptionsList()[1].toObject();
    }
  }
}

@Injectable()
export class GameListService {
  private map: Map<number, GameAccessor>;
  constructor(private apiMethods: ApiMethods) {}

  private async getMap(): Promise<Map<number, GameAccessor>> {
    if (this.map) {
      return this.map;
    }

    return this.updateMap();
  }

  private async updateMap(): Promise<Map<number, GameAccessor>> {
    const games = await this.apiMethods.fetchAvailableGames();
    this.map = new Map();
    games.forEach((game) => {
      this.map.set(game.getId() as number, new GameAccessor(game));
    });

    return this.map;
  }

  public async getGames(): Promise<Array<GameAccessor>> {
    const array = new Array<GameAccessor>();
    (await this.getMap()).forEach((value) => array.push(value));

    return array;
  }

  public async getGame(gameId: number): Promise<GameAccessor | undefined> {
    return (await this.getMap()).get(gameId);
  }
}
