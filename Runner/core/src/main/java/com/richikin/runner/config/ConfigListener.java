package com.richikin.runner.config;

import com.richikin.runner.audio.AudioData;
import com.richikin.runner.audio.GameAudio;
import com.richikin.runner.core.App;

public class ConfigListener
{
    public ConfigListener()
    {
    }

    public void update()
    {
        if (App.settings.isEnabled(Settings._MUSIC_ENABLED))
        {
            if (AppConfig.gameScreenActive())
            {
                if (!GameAudio.inst().isTunePlaying(AudioData.MUS_GAME))
                {
                    GameAudio.inst().playGameTune(true);
                }
            }
        }
        else
        {
            GameAudio.inst().tuneStop();
        }

        GameAudio.inst().setFXVolume(App.settings.isEnabled(Settings._SOUNDS_ENABLED) ? AudioData._DEFAULT_FX_VOLUME : 0);
    }
}
