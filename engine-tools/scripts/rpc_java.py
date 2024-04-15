from py4j.java_gateway import JavaGateway

gateway = JavaGateway() 

fitnessFn = gateway.entry_point

material=756
position=204
expansion=27
ataque=13

evaluation = fitnessFn.fitness(material, position, expansion, ataque)

print(evaluation)
