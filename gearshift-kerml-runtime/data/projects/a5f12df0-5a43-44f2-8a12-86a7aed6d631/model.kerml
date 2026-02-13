package Vehicle {
  class Engine {
    feature cylinders : Integer = 4;
  }

  class Car specializes Vehicle {
    composite feature engine : Engine;
    feature maxSpeed : Integer;
  }
}
