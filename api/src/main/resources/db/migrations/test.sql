SELECT * FROM "user" u INNER JOIN bonus_card bc ON bc.user_id = u.id
         WHERE bc.number = '1234567' AND bc.good_thru_month = '12' AND bc.good_thru_year = '2014';

SELECT * FROM bonus_card;