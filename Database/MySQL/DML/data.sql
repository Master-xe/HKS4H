
LOCK TABLES settings WRITE;
INSERT INTO settings VALUES
('pacurlws', 'https://services.sw.com.mx', null, null),
('pactoken', 'T2lYQ0t4L0RHVkR4dHZ5Nkk1VHNEakZ3Y0J4Nk9GODZuRyt4cE1wVm5tbXB3YVZxTHdOdHAwVXY2NTdJb1hkREtXTzE3dk9pMmdMdkFDR2xFWFVPUXpTUm9mTG1ySXdZbFNja3FRa0RlYURqbzdzdlI2UUx1WGJiKzViUWY2dnZGbFloUDJ6RjhFTGF4M1BySnJ4cHF0YjUvbmRyWWpjTkVLN3ppd3RxL0dJPQ.T2lYQ0t4L0RHVkR4dHZ5Nkk1VHNEakZ3Y0J4Nk9GODZuRyt4cE1wVm5tbFlVcU92YUJTZWlHU3pER1kySnlXRTF4alNUS0ZWcUlVS0NhelhqaXdnWTRncklVSWVvZlFZMWNyUjVxYUFxMWFxcStUL1IzdGpHRTJqdS9Zakw2UGRtMXkzWlpwQ2MyUjNhNVpZQ3BTM01VR2NESDI3RHoraVVBR0R1dEUyU0FjeUI3S2p2aXlQaDlDVWV5dityQnFjSjd3WTBOVmI2UE85bTJBdVhTeEJkdktRWGhKR1lwUHY3ZUdLK1RMSEFpbGo3bmt3VXpka0o0VzlWQUFORkp4QUJpMnI3UHpzZk5nenBzbDBCbTk3QmduTkhDMHhwTzZnWHNOTlRXUjI5Q0VxZmxSUXJtUFFDYy9IRnJsRjVubTdtRitHVE14SFJ0ZHN3aW9STnFrYmJ6NzMwV2RmRG5qQlZrRTZ5aWJzMnlCZnlVNzJ1L1RuM2pqYzF4cVRHekJWTWRQZzQzYk91OXdESHo2VjZZd1lCaktZWU5xbHE1MWZJNUMxWExPVXlOKzkray9YZHpPUjY5YjI4SWhiL1Zpa3lXS3hQdjgwY2JRYk5KM2VkT3ExdEgzeDJZaTUveTdxQlg1UmwraGlhRkNLOVVaWFBRVHhHem9USmV2MkR0RjNrdEwrWDhnQ2laWWVVdHlOdUdqN3hFbHEvTUNFc2lRYmIwaXR6ZUVZTEVrPQ.ShgzUMPcg3pCpiVoSIaGG1bjdRwlxtlVzRsq4t_HI-M', null, null),
('koneshws', 'https://198.61.151.213/axis2/services/KWSInformacionComplementariaSAPECC', null, null),
('workspace', '/opt/portalcfdi/', null, null),
('runmode', 'P', null, null);
UNLOCK TABLES;

LOCK TABLES profiles WRITE;
INSERT INTO profiles VALUES
(1, 'System', 'Defined user for automatic tasks.'),
(2, 'Admin', 'Admin user for manage application.'),
(3, 'Fiscal', 'Fiscal profile to take decisions.'),
(4, 'Expins', 'User to only view the information.');
UNLOCK TABLES;

LOCK TABLES users WRITE;
INSERT INTO users VALUES
(1, 1, 'system@domain.com', 'System', 'Sys', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 0, 'Y', null, now()),
(2, 2, 'admin@domain.com', 'Admin', 'Admin', 'c12e09d792c07f2cce79df4bdc75af5eed184165fc9571230691ae91a7ec2ced', 0, 'N', null, now());
UNLOCK TABLES;

LOCK TABLES passwords WRITE;
INSERT INTO passwords VALUES
(1, 2, 'c12e09d792c07f2cce79df4bdc75af5eed184165fc9571230691ae91a7ec2ced');
UNLOCK TABLES;
-- [Horario|Frecuencia|(NDíasHora:Minuto)]
LOCK TABLES crones WRITE;
INSERT INTO crones VALUES
('CCFDI_REQUESTS', 'H',	'1D12:00'),
('KONESH_REQUEST', 'F', '1'),
('UUID_REQUESTS', 'F', '1'),
('VERIFY_AND_DOWNLOAD', 'F', '1');
UNLOCK TABLES;

