import os
import requests
import openai

# Настройки
GITLAB_API = "https://gitlab.com/api/v4"
PROJECT_ID = os.environ["CI_PROJECT_ID"]
MR_IID = os.environ["CI_MERGE_REQUEST_IID"]
GITLAB_TOKEN = os.environ["GITLAB_TOKEN"]
OPENAI_API_KEY = os.environ["OPENAI_API_KEY"]

openai.api_key = OPENAI_API_KEY

headers_gitlab = {
    "PRIVATE-TOKEN": GITLAB_TOKEN
}

def get_mr_diff():
    url = f"{GITLAB_API}/projects/{PROJECT_ID}/merge_requests/{MR_IID}/changes"
    r = requests.get(url, headers=headers_gitlab)
    r.raise_for_status()
    changes = r.json()["changes"]
    diff_text = "\n".join(
        f"{c['new_path']}\n{c['diff']}" for c in changes if 'diff' in c
    )
    return diff_text[:12000]  # ограничение на токены

def ask_chatgpt(diff):
    messages = [
        {"role": "system", "content": "Ты — бот, выполняющий ревью кода в GitLab Merge Request'ах. Анализируй безопасность, читаемость, архитектуру, стиль и тестирование. Возвращай краткий, но структурированный вывод."},
        {"role": "user", "content": f"Вот diff изменений в MR:\n{diff}"}
    ]
    response = openai.ChatCompletion.create(
        model="gpt-4",
        messages=messages
    )
    return response["choices"][0]["message"]["content"]

def post_comment(body):
    url = f"{GITLAB_API}/projects/{PROJECT_ID}/merge_requests/{MR_IID}/notes"
    response = requests.post(url, headers=headers_gitlab, json={"body": body})
    response.raise_for_status()
    print("Комментарий добавлен:", response.json()["body"])

if __name__ == "__main__":
    print("📥 Получение diff...")
    diff = get_mr_diff()
    print("🤖 Отправка в ChatGPT...")
    review = ask_chatgpt(diff)
    print("📤 Публикация комментария...")
    post_comment(review)
