from bayes_opt import BayesianOptimization
from py4j.java_gateway import JavaGateway


gateway = JavaGateway() 

javaObj = gateway.entry_point

def black_box_function(scalar1, scalar2, scalar3):
    return javaObj.fitness(scalar1, scalar2, scalar3)

# Bounded region of parameter space
pbounds = {'scalar1': (0, 1000), 'scalar2': (0, 1000), 'scalar3': (0, 1000)}


optimizer = BayesianOptimization(
    f=black_box_function,
    pbounds=pbounds,
    verbose=2, # verbose = 1 prints only when a maximum is observed, verbose = 0 is silent
    random_state=1,
)

optimizer.maximize(
    init_points=5,
    n_iter=1000,
)

javaObj.endWork()

print(optimizer.max)



