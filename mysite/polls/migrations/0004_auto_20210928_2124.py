# Generated by Django 3.2.7 on 2021-09-28 18:24

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('polls', '0003_alter_car_brand'),
    ]

    operations = [
        migrations.AddField(
            model_name='car',
            name='model',
            field=models.CharField(default='unknown', editable=False, max_length=50),
        ),
        migrations.AlterField(
            model_name='car',
            name='color',
            field=models.CharField(max_length=10),
        ),
    ]
