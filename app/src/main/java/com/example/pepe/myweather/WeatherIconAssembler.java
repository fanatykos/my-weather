package com.example.pepe.myweather;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import java.util.Date;


public class WeatherIconAssembler {
    int icon = 0;
    int weatherIconColor = 0;

    public void setWeatherIcon(int actualId, long sunrise, long sunset, TextView weatherIcon) {
        int id = actualId / 100;
        long currentTime = new Date().getTime();
        boolean isADay = (currentTime >= sunrise && currentTime < sunset);
        if (isADay) {
            weatherIconColor = R.color.sunny;
        } else {
            weatherIconColor = R.color.night;
        }
        switch (id) {
            case 2:
                setThunderIcon(actualId, isADay);
                break;
            case 3:
                setDrizzleIcon(actualId, isADay);
                break;
            case 5:
                setRainIcon(actualId, isADay);
                break;
            case 6:
                setSnowIcon(actualId, isADay);
                break;
            case 7:
                setAtmosphereIcon(actualId, isADay);
                break;
            case 8:
                setCloudsIcon(actualId, isADay);
                break;
            case 9:
                setExtremeIcon(actualId);
                break;
        }
        weatherIcon.setTextColor(ContextCompat.getColor(weatherIcon.getContext(), weatherIconColor));
        weatherIcon.setText(icon);
    }

    private void setThunderIcon(int actualId, boolean isADay) {
        if (actualId == 200 || actualId == 201 || actualId == 230 || actualId == 231) {
            if (isADay) {
                icon = R.string.day_storm_showers;
            } else {
                icon = R.string.night_storm_showers;
            }
            return;
        }
        if (actualId == 202 || actualId == 232) {
            if (isADay) {
                icon = R.string.day_thunderstorm;
            } else {
                icon = R.string.night_thunderstorm;
            }
            return;
        }
        if (actualId == 210) {
            if (isADay) {
                icon = R.string.day_lightning;
            } else {
                icon = R.string.night_lightning;
            }
        } else {
            if (isADay) {
                weatherIconColor = R.color.cloudyDay;
            }
            icon = R.string.heavy_thunderstorm;
        }
    }


    private void setDrizzleIcon(int actualId, boolean isADay) {
        if (actualId == 300 || actualId == 310 || actualId == 313) {
            if (isADay) {
                icon = R.string.day_drizzle;
            } else {
                icon = R.string.night_drizzle;
            }
        } else {
            if (isADay) {
                weatherIconColor = R.color.cloudyDay;
            }
            icon = R.string.heavy_drizzle;
        }
    }

    private void setRainIcon(int actualId, boolean isADay) {
        if (actualId == 500 || actualId == 520) {
            if (isADay) {
                icon = R.string.day_drizzle;
            } else {
                icon = R.string.night_drizzle;
            }
            return;
        }
        if (actualId == 501 || actualId == 521) {
            if (isADay) {
                icon = R.string.day_rainy;
            } else {
                icon = R.string.night_rainy;
            }
        } else {
            if (isADay) {
                weatherIconColor = R.color.cloudyDay;
            }
            icon = R.string.heavy_rainy;
        }
    }

    private void setSnowIcon(int actualId, boolean isADay) {
        if (actualId == 600 || actualId == 620 || actualId == 601 || actualId == 621) {
            if (isADay) {
                icon = R.string.day_snowy;

            } else {
                icon = R.string.night_snowy;
            }
            return;
        }
        if (actualId == 602 || actualId == 622) {
            if (isADay) {
                weatherIconColor = R.color.cloudyDay;
            }
            icon = R.string.haevy_snowy;
        } else {
            if (isADay) {
                icon = R.string.day_sleet;
            } else {
                icon = R.string.nigt_sleet;
            }
        }
    }

    private void setAtmosphereIcon(int actualId, boolean isADay) {
        if (isADay) {
            weatherIconColor = R.color.cloudyDay;
        }
        icon = R.string.foggy;
    }

    private void setCloudsIcon(int actualId, boolean isADay) {
        if (actualId == 800) {
            if (isADay) {
                icon = R.string.day_clear;

            } else {
                icon = R.string.night_clear;
            }
            return;
        }
        if (actualId == 801) {
            if (isADay) {
                icon = R.string.day_partly_cloudy;

            } else {
                icon = R.string.night_partly_cloudy;
            }
            return;
        }
        if (actualId == 802) {
            if (isADay) {
                icon = R.string.day_cloudy;

            } else {
                icon = R.string.night_cloudy;
            }
        } else {
            if (isADay) {
                weatherIconColor = R.color.cloudyDay;
            }
            icon = R.string.cloudy;
        }
    }

    private void setExtremeIcon(int actualId) {
        weatherIconColor = R.color.warnings;
        if (actualId == 900) {
            icon = R.string.tornado;
            return;
        }
        if (actualId == 901) {
            icon = R.string.storm;
            return;
        }
        if (actualId == 902) {
            icon = R.string.hurricane;
            return;
        }
        if (actualId == 903) {
            icon = R.string.hurricane;
            return;
        }
    }
}
