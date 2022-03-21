# www88899
Config files for my GitHub profile.

Description
Now it's time to add some query commands.
The command reg_by_color prints all registration numbers of cars of a particular color, taking color as a parameter. The color may be written in uppercase or
lowercase letters. For example, reg_by_color BLACK. The answer should contain registration numbers separated by commas. The order should be the same as in 
the status command. If there are no cars of this color, the output should be like this: No cars with color BLACK were found..
The command spot_by_color is similar to the previous one, but it prints the parking space numbers of all the cars of a particular color.
The command spot_by_reg returns you the number of the spot where a car is located based on its registration number, for example, spot_by_reg KA-01. 
This command will also return an error message if your car was not found: No cars with registration number KA-01 were found. For convenience,
let's suppose that all car registration numbers are unique.

Example
The symbol > represents the user input.

> spot_by_color yellow
Sorry, a parking lot has not been created.
> create 4
Created a parking lot with 4 spots.
> park KA-01-HH-9999 White
White car parked in spot 1.
> park KA-01-HH-3672 White
White car parked in spot 2.
> park Rs-P-N-21 Red
Red car parked in spot 3.
> park Rs-P-N-22 Red
Red car parked in spot 4.
> reg_by_color GREEN
No cars with color GREEN were found.
> reg_by_color WHITE
KA-01-HH-9999, KA-01-HH-3672
> spot_by_color GREEN
No cars with color GREEN were found.
> spot_by_color red
3, 4
> spot_by_reg ABC
No cars with registration number ABC were found.
> spot_by_reg KA-01-HH-3672
2
> spot_by_reg Rs-P-N-21
3
> exit

