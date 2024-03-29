## AML Camunda - casernkb

### Environments
- {+ SERVER_PORT - порт приложения +}
- {+ SPRING_APPLICATION_NAME - наименование сервиса: casernkb - +}

- {+ DATABASE_SHOWSQL - +}
- {+ DATABASE_URL - +}
- {+ DATABASE_USERNAME - +} 
- {+ DATABASE_PASSWORD - +}
- {+ DATABASE_DRIVER - драйвер для подключения к БД +}
- {+ DATABASE_PLATFORM - :org.hibernate.dialect.Oracle12cDialect +}
- {+ DATABASE_MAX_POOL_SIZE - максимальное кол-во сессий к БД +}

- {+ LOG_PATH - путь для логирование:/home/af_butenkoav/casernkb/log/app.log +}

- {+ CAMUNDA_ID - The username (e.g., 'admin') +}
- {+ CAMUNDA_PASSWORD - The initial password +}
- {+ CAMUNDA_FIRST_NAME - Additional (optional) user attributes +} 
- {+ CAMUNDA_LAST_NAME - Additional (optional) user attributes +}
- {+ CAMUNDA_DATABASE_TYPE - Type of the underlying database. Possible values: h2, mysql, mariadb, oracle, postgres, mssql, db2 +}
- {+ CAMUNDA_SCHEMA_UPDATE - If automatic schema update should be applied, use one of [true, false, create, create-drop, drop-create] +}
- {+ FILES_SCHEDULER_RETENTION - fixed interval of time for job ms +}

- {+ MINIO_ENDPOINT - URL для MinIO +}
- {+ MINIO_CASE_FOLDER - с какой папки брать файл для создания кейса +}
- {+ MINIO_FILE_NAME - название файла с расширением +}
- {+ MINIO_ERR0R_CASE_FOLDER - папка на складывание ошибочных файлов +}
- {+ MINIO_SUCCESS_CASE_FOLDER - папка для успешно отработтаных фйалов +}
- {+ MINIO_PORT - +}
- {+ MINIO_SECURE - использовать https [true, false] +}
- {+ MINIO_ACCESS_KEY - +}
- {+ MINIO_SECRET_KEY - +}
- {+ MINIO_BUCKET_NAME - +}
- {+ MINIO_FILE_SIZE - разрешенный размер файла +}

- {+ KAFKA_BROKERS - Kafka brokers +}
- {+ KAFKA_SECURITY_PROTOCOL - Тип протокола (ssh) +}
- {+ KAFKA_SSL_TRUSTSTORE_LOCATION - Путь к truststore +}
- {+ KAFKA_SSL_TRUSTSTORE_PASSWORD - Пароль +}
- {+ KAFKA_SSL_KEYSTORE_TYPE - Тип keystore (jks) +}
- {+ KAFKA_SSL_KEYSTORE_LOCATION - Путь к keystore +}
- {+ KAFKA_SSL_KEYSTORE_PASSWORD - Пароль +}
- {+ KAFKA_SSL_KEY_PASSWORD - Пароль +}

- {+ TOPIC_MESSAGE_INPUT - Топик для входящих сообщений +}
- {+ TOPIC_CSM_INPUT - Топик от CSM +}
- {+ TOPIC_MESSAGE_OUT - Тип исходящих сообщений +}
- {+ TOPIC_ERROR_OUT - Топик для отладки и просмотра ошибок запуска процессов от camunda +}
- MINIO_REEST_FOLDER - папка в minio для сохранения файлов реестра
- LEGAL_DOC_PATTERN - Путь к шаблону ЮЛ
- INDIVIDUAL_DOC_PATTERN - Путь к шаблону ФЛ
- TOPIC_RISK_RESPONSE_SAVE_INPUT - Топик для чтения сообщений по обработке рисков
- TOPIC_KYC_CASE_CREATION - Топик входящих сообщений для формирования кейса KYC
- TOPIC_ZK_REQUEST - Топик входящих сообщений по спецификации запроса ЗК
- TOPIC_ZK_RESPONSE_IN - Топик входящих сообщений по спецификации ответа ЗК
- TOPIC_ZK_STATUS - Топик входящих сообщений по спецификации смены статуса ЗК
- TOPIC_ZK_REQUEST_OUT - Топик исходящих сообщений по спецификации запроса ЗК
- TOPIC_ZK_RESPONSE_OUT - Топик исходящих сообщений по спецификации ответа ЗК