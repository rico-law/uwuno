import React from "react";
import { Box } from "../../components/Box/Box";
import { GameSettingForm } from "../../components/GameSettingForm/GameSettingForm";
import "./Lobby.css";

function Lobby() {
  return (
    <div className="lobby">
      <Box>
        <h3>game settings</h3>
        <GameSettingForm />
      </Box>
    </div>
  );
}

export default Lobby;
