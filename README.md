Spring Batch - подпроект Spring для выполнения пакетных задач.
В этом примере Batch используется для переноса данных о книгах из Jpa репозитория в Mongo

Чтобы начать:
1. implementation 'org.springframework.boot:spring-boot-starter-batch'
2. Отключить автоматическое выполнение Job при старте: spring.batch.job.enabled: false

Команды Spring Shell:
1. da - удалить все записи из Mongo;
2. job - запустить работу по миграции;
3. ab - получить все записи за Mongo;

Job - собственно сама работа, которую необходимо сделать.
Создается с помощью JobBuilderFactory
В Билдер передаем Step. В этом примере шаг - это работа по миграции данных из одной БД в другую.
Также можно указать шаг, который будет выполняться после завершения предыдущего шага - .next(Step step)

Параметры Шага:
1. Chunk - объем информации, которая будет обрабатываться за раз (item). В данном случае - количество записей.
Chunk имеет параметры: принимаемый и ожидаемый на выходе тип данных.
2. Reader - интерфейс для чтения данных
3. Processor - тут происходит обработка item, полученного в Reader
4. Writer - записывает обработанный item

Spring Batch содержит много реализованных Ридеров и Врайтеров, самим писать не надо

Ссылки:
1. Список реализованных Ридеров и Врайтеров:
https://docs.spring.io/spring-batch/docs/current/reference/html/appendix.html#listOfReadersAndWriters
2. Видео по Batch и презентация
https://oc.phoenixit.ru/index.php/apps/files/?dir=/Spring/28.%20Spring%20Batch&fileid=19354328
3. Пример. Тут информация считывается из csv и пишется в БД
https://javainside.ru/primer-ispolzovaniya-spring-batch-3-0-chast-1/
https://javainside.ru/spring-batch-3-0-chast-2-itemreader-itempocessor-itemwriter/
4. Документация
https://spring.io/guides/gs/batch-processing/
5. Тасклеты
https://docs.spring.io/spring-batch/docs/current/reference/html/step.html#taskletStep
6. Параллельное выполнение шагов
https://docs.spring.io/spring-batch/docs/current/reference/html/scalability.html#scalabilityParallelSteps
