SELECT *FROM "user" u
    INNER JOIN bonus_card bc ON bc.user_id = u.id
        WHERE bc.number = '9876543210987654' AND bc.good_thru_month = '12' AND bc.good_thru_year = '2014'