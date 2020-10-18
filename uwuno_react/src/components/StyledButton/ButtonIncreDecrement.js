import React from "react";
import { Button, ButtonGroup, makeStyles } from "@material-ui/core";

export const ButtonIncreDecrement = ({
  colour,
  groupColour,
  variant = "contained",
}) => {
  const useStyles = makeStyles({
    root: {
      background: colour,
    },
    label: {
      color: "black",
    },
  });

  const classes = useStyles();

  return (
    <ButtonGroup
      orientation="vertical"
      color={groupColour}
      aria-label="vertical contained primary button group"
      variant={variant}
    >
      <Button
        classes={{
          root: classes.root,
          label: classes.label,
        }}
        variant={variant}
      >
        +
      </Button>
      <Button
        classes={{
          root: classes.root,
          label: classes.label,
        }}
        variant={variant}
      >
        -
      </Button>
    </ButtonGroup>
  );
};