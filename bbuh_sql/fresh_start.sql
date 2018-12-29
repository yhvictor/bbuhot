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
  winning_bet_option INT NOT NULL DEFAULT '-2',
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

DROP TABLE IF EXISTS bbuhot_bets;
CREATE TABLE bbuhot_bets (
  id INT NOT NULL AUTO_INCREMENT,
-- TODO(luciusgone): find ways to add foreign key constraint for use_id
  user_id mediumint(8) unsigned NOT NULL DEFAULT '0',
  betting_option_id INT NOT NULL DEFAULT '0',
  bet_amount INT NOT NULL DEFAULT '0',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (betting_option_id)
      REFERENCES bbuhot_betting_options(id)
) ENGINE = INNODB;

DROP TABLE IF EXISTS bbuhot_paid_bets;
CREATE TABLE bbuhot_paid_bets (
  id INT NOT NULL AUTO_INCREMENT,
-- TODO(luciusgone): find ways to add foreign key constraint for user_id
  user_id mediumint(8) unsigned NOT NULL DEFAULT '0',
  betting_option_id INT NOT NULL DEFAULT '0',
  bet_amount INT NOT NULL DEFAULT '0',
  earning INT NOT NULL DEFAULT '0',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (betting_option_id)
      REFERENCES bbuhot_betting_options(id)
) ENGINE = INNODB;
