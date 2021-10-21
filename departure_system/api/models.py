from django.db import models

# Create your models here.
class Train(models.Model):
    train_id = models.CharField(max_length=20)
    departure_time = models.CharField(max_length=100)
    destination = models.CharField(max_length=50)
    status = models.CharField(max_length=50)
    