# CallerforGate
App for automatic dial-up to the subscriber (gate-barrier)





При въезде на парковку у дома присутствует автоматический шлагбаум, который открывается по звонку на специальный номер или с брелка. Мне удобно приближаясь к дому взять телефон, разблокировать его и тапнуть иконку - которая запускает дозвон до абонента (это достаточно безопасно и выполняется машинально - не глядя).
И вот сменив однажды смартфон - оказалось невозможным расположить иконку абонента на "рабочем столе", т.е. абонента расположить можно, но это будет ссылка на абонента, а не вызов абонента.
В плеймаркете ничего подходящего с таким элементарным функционалом я не нашел, пришлось писать самому.

Получилось приложение для автоматического вызова абонента при его запуске (для устройств с несколькими SIM картами рекомендуется включить sim по умолчанию, чтобы не выскакивало окно с вопросом). Вызов осуществляется с использованием штатного диалера, ввод нового абонента выполняется вручную (справочник не прикручен).

1. Тап на иконке приложения в первый раз запускает приложение с экраном настройки, где нужно ввести номер для автоматического вызова.
2. Если номер указан ранее - Тап на иконке приложения автоматически стартует штатный диалер и осуществляет звонок по заданному номеру.
3. При прошествии 3 секунд приложение "сворачивается", чтобы его никогда не было видно при ежедневном использовании.
4. Если при запуске приложения начался вызов диалером и сразу при его открытии нажать "назад" (успев это сделать за 3 секунды после запуска) - то будет доступно окно приложения, где можно будет изменить настройки (окно уже не будет закрываться автоматически).

Каждый раз изменив номер для дозвона или введя его первый раз - нужно нажать кнопку сохранения настроек.


![screenshot](https://github.com/ushanovsn/CounterHTML/blob/master/.pic/screenshot.jpg?raw=true)
