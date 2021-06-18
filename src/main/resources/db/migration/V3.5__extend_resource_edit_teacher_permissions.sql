UPDATE `welearn`.`roles` SET `permissions` = '[[1,1,1,0,0],[4,0,1,0,0],[5,1,1,0,0],[6,1,1,0,0]]' WHERE (`id` = '2');

ALTER TABLE `resource`
	DROP FOREIGN KEY `resource_schedule`;

ALTER TABLE `resource`
	CHANGE COLUMN `schedule_id` `event_id` VARCHAR(45) NULL DEFAULT NULL COLLATE 'utf16_general_ci' AFTER `discipline_id`,
	DROP INDEX `resource_schedule`,
	ADD INDEX `resource_schedule` (`event_id`) USING BTREE;

ALTER TABLE `resource`
	ADD CONSTRAINT `resource_event` FOREIGN KEY (`event_id`) REFERENCES `event` (`event_id`);
