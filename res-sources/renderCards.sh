#!/bin/bash

mkdir tmp
for value in `seq 0 12`
  do
  convert -background none value_$value.png -colorize 0,100,100 tmp/value_0_$value.png
  convert -background none value_$value.png -colorize 0,100,100 tmp/value_1_$value.png
  convert -background none value_$value.png -colorize 100,100,100 tmp/value_2_$value.png
  convert -background none value_$value.png -colorize 100,100,100 tmp/value_3_$value.png
  for color in `seq 0 3`
	do
	convert -background none 'tmp/value_'$color'_'$value'.png' -rotate 180 'tmp/value_rotated_'$color'_'$value'.png'
  done
done
for color in `seq 0 3`
  do
  for value in `seq 0 12`
	do
	convert -background none front_base.png \
	  -draw 'image SrcOver 0,0 0,0 tmp/value_'$color'_'$value'.png' \
	  -draw 'image SrcOver 16,32 0,0 color_'$color'.png' \
	  -draw 'image SrcOver 32,64 0,0 tmp/value_rotated_'$color'_'$value'.png' \
	  '../assets/gfx/cards/color'$color'_value'$value'.png'
  done
done
rm -Rf tmp/
