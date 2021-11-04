import numpy as np
import matplotlib.pyplot as plt
from PIL import Image

img = Image.open('low_contrast.jpg')


def cumulative_sum(a):
    a = iter(a)
    b = [next(a)]
    for i in a:
        b.append(b[-1] + i)

    return np.array(b)


def get_histogram(image, bins):
    hist = np.zeros(bins)

    for pixel in image:
        hist[pixel] += 1

    return hist


def normalize_sum(sum):
    nj = (sum - sum.min()) * 255
    n = sum.max() - sum.min()

    return nj / n


def compare_the_two_images(img1, img2):
    fig = plt.figure()
    fig.set_figheight(15)
    fig.set_figwidth(15)

    fig.add_subplot(1, 2, 1)
    plt.imshow(img1, cmap='gray')

    fig.add_subplot(1, 2, 2)
    plt.imshow(img2, cmap='gray')

    plt.show(block=True)


def equalize_histogram(image):

    image = np.asarray(image)
    # 0(black)-255(white)
    flat = image.flatten()

    hist = get_histogram(flat, 256)
    sum = cumulative_sum(hist)
    sum = normalize_sum(sum)
    sum = sum.astype('uint8')

    new_image = sum[flat]
    new_image = np.reshape(new_image, image.shape)

    return new_image


if __name__ == '__main__':
    result = equalize_histogram(img)
    compare_the_two_images(img, result)
