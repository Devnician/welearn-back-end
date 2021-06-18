ALTER TABLE `resource`
    MODIFY `dir_path` varchar(255);

ALTER TABLE `resource`
	DROP INDEX `dir_path`;

