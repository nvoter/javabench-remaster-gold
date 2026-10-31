# JavaBench Remaster: gold-подмножество

Воспроизводимое подмножество JavaBench для экспериментов по агентному дополнению и исправлению Java-кода.

В репозитории находятся 100 задач на дополнение одного класса, прошедших gold-валидацию, из трёх Java-проектов:

| Проект | Число задач |
| --- | ---: |
| PA02 — Spring Cloud Alibaba | 33 |
| PA03 — Alibaba Druid | 33 |
| PA04 — Apache Commons Lang | 34 |
| **Всего** | **100** |

Список выбранных задач находится в [`datasets/subset_gold_valid.json`](datasets/subset_gold_valid.json). Для всех 100 задач исходный эталонный код был точно восстановлен и успешно прошёл сборку и тесты. Результат этой проверки сохранён в [`validation/gold_validation.json`](validation/gold_validation.json).

## Содержимое

- `datasets/` — задачи с minimum-, maximum- и selective-context, дескрипторы, gold-данные и зафиксированное подмножество из 100 задач;
- `projects/` — незавершённые проекты (`PAxx`) и эталонные версии (`PAxx-Solution`), которые использует оценщик;
- `app/`, `evaluation.py`, `parser.py` — код подготовки данных и поклассовой оценки JavaBench;
- `validation/` — зафиксированный результат gold-валидации.

## Требования

- Python 3.9 или новее;
- JDK 21;
- Maven в `PATH`.

Создание окружения и установка зависимостей:

```bash
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
```

## Проверка gold-подмножества

В корне репозитория:

```bash
PYTHONPATH=. python -m app.validate_gold --subset datasets/subset_gold_valid.json
```

Успешный запуск завершается строками:

```text
Gold valid:   100
Gold invalid: 0
```

Проверка собирает проекты и запускает их тесты, поэтому занимает несколько минут. При первом запуске Maven может загрузить зависимости.

## Оценка сгенерированных решений

Входной файл должен быть JSONL в формате samples JavaBench. Поклассовая оценка:

```bash
python evaluation.py class-wise generated-samples.jsonl --output score.json
```

Для каждого кандидата оценщик независимо подставляет сгенерированный класс в соответствующий проект `PAxx-Solution`, после чего компилирует проект и запускает тесты. Поле `compile_errors` определяет успешность компиляции; результаты тестов доступны только для скомпилировавшихся кандидатов.
