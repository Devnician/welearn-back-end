ALTER TABLE `users`
    MODIFY `email` varchar(45) UNIQUE NOT NULL;

ALTER TABLE `users`
    MODIFY `username` varchar(45) UNIQUE NOT NULL;

ALTER TABLE `users`
    MODIFY `role_id` int(6) NOT NULL;

ALTER TABLE `schedule`
    DROP COLUMN `day_id`;

ALTER TABLE `roles`
    MODIFY `role` varchar(45) NOT NULL UNIQUE;

ALTER TABLE `resource`
    MODIFY `dir_path` varchar(45) NOT NULL UNIQUE;