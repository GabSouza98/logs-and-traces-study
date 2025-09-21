SELECT * FROM mytest.control_table WITH(UPDLOCK, HOLDLOCK)
WHERE uuid = :uuid;