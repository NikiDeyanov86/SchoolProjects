from django.db import models

# Create your models here.
class Car(models.Model):
    color = models.CharField(max_length=20)
    made = models.DateTimeField(auto_now_add=True)
    brand = models.CharField(max_length=50)
    model = models.CharField(max_length=50, default="", editable=True)
    description = models.TextField()