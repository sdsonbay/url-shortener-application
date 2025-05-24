from locust import HttpUser, task, between
import random

class UrlShortenerUser(HttpUser):
    wait_time = between(1, 3)

    @task(3)
    def shorten_url(self):
        random_url = f"https://example.com/{random.randint(1, 100000)}"
        self.client.post(
            "/api/v1/url/shorten",
            json={"url": random_url},
            headers={"Content-Type": "application/json"}
        )

    @task(1)
    def get_description(self):
        self.client.get("/api/v1/url/desc")