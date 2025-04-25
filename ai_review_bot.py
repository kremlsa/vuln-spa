import os
import requests
from openai import OpenAI

# 🔐 Получаем переменные окружения
GITLAB_API = "https://gitlab.com/api/v4"
PROJECT_ID = os.environ["CI_PROJECT_ID"]
MR_IID = os.environ["CI_MERGE_REQUEST_IID"]
GITLAB_TOKEN = os.environ["GITLAB_TOKEN"]
OPENAI_API_KEY = os.environ["OPENAI_API_KEY"]

# 🧠 Инициализация клиента OpenAI
client = OpenAI(api_key=OPENAI_API_KEY)

# 📡 Заголовки для GitLab API
headers_gitlab = {
    "PRIVATE-TOKEN": GITLAB_TOKEN
}

def get_mr_diff():
    url = f"{GITLAB_API}/projects/{PROJECT_ID}/merge_requests/{MR_IID}/changes"
    response = requests.get(url, headers=headers_gitlab)
    response.raise_for_status()
    changes = response.json()["changes"]
    return "\n".join(f"File: {c['new_path']}\n{c['diff']}" for c in changes if 'diff' in c)[:12000]

def ask_chatgpt(diff_text):
    messages = [
        {"role": "system", "content": "Ты — код-ревьюер. Проанализируй изменения в GitLab Merge Request. Оцени безопасность, читаемость, архитектуру, стиль и тестирование. Ответ дай в Markdown-формате: с заголовками, эмодзи, списками."},
        {"role": "user", "content": f"Вот diff кода:\n{diff_text}"}
    ]
    response = client.chat.completions.create(
        model="gpt-4",
        messages=messages
    )
    return response.choices[0].message.content

def post_comment(body):
    url = f"{GITLAB_API}/projects/{PROJECT_ID}/merge_requests/{MR_IID}/notes"
    response = requests.post(url, headers=headers_gitlab, json={"body": body})
    response.raise_for_status()
    print("✅ Комментарий добавлен:", response.json()["body"][:80])

if __name__ == "__main__":
    print("📥 Получаем изменения MR...")
    diff = get_mr_diff()
    print("💡 Отправляем на ревью GPT...")
    review = ask_chatgpt(diff)
    print("📤 Публикуем комментарий в MR...")
    post_comment(review)
