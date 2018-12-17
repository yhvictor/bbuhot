use ultrax;

DROP TABLE bbuhot_betting_options;
DROP TABLE bbuhot_game;

CREATE TABLE bbuhot_game (
  id INT NOT NULL AUTO_INCREMENT,
  name TEXT NOT NULL DEFAULT '',
  description TEXT NOT NULL DEFAULT '',
  normal_user_visible TINYINT(1) NOT NULL DEFAULT '0',
  status TINYINT NOT NULL DEFAULT '0',
  bet_option_limit TINYINT NOT NULL DEFAULT '1',
  bet_amount_lowest INT NOT NULL DEFAULT '0',
  bet_amount_highest INT NOT NULL DEFAULT '0',
  end_time_ms TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY(id)
) ENGINE = INNODB;

CREATE TABLE bbuhot_betting_options (
  id INT NOT NULL AUTO_INCREMENT,
  game_id INT, # Have to keep this nullable to allow hibernate to delete.
  name TEXT NOT NULL DEFAULT '',
  odds INT NOT NULL DEFAULT '1000000',
  INDEX game_index (game_id),
  PRIMARY KEY (id),
  FOREIGN KEY (game_id)
      REFERENCES bbuhot_game(id)
      ON DELETE CASCADE
) ENGINE = INNODB;
