import React from 'react';
import TextField from '@material-ui/core/TextField';
import { makeStyles } from '@material-ui/core/styles';
import { ButtonIncreDecrement as Button } from "../StyledButton/ButtonIncreDecrement";

export const StyledTextField = ({
  background,
  textColour,
  label,
  variant = "outlined",
}) => {
  const useStyles = makeStyles({
    root: {
      '& .MuiTextField-root': {
        width: '25ch',
      },
    },
    label: {
      textTransform: "lowercase",
      // textColor: "black",
      color: background,
    }
  });

  const classes = useStyles();
  
  return (
    <form className={classes.root} noValidate autoComplete="off">
      <div>
        {label}
        <TextField
          id="outlined-number"
          // label="Number"
          // type="number" TODO: how to get rid of extra set of buttons
          InputLabelProps={{
            shrink: true,
          }}
          InputProps={{
            endAdornment: <Button
                            colour="#FF69B4"
                            groupColour="secondary"
                          />
          }}
          classes={{
            root: classes.root,
            label: classes.label,
          }}
          variant={variant}
        />
      </div>
    </form>
  );
};