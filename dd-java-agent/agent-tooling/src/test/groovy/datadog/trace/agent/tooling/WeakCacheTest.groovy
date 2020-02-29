package datadog.trace.agent.tooling

import spock.lang.Specification

import java.util.concurrent.Callable

class WeakCacheTest  extends Specification  {
  def supplier = new CounterSupplier()

  def sut = new WeakCache().newWeakCache()

  def "getOrCreate a value"() {
    when:
    def count = sut.get('key', supplier)

    then:
    count == 1
    supplier.counter == 1
    sut.cache.size() == 1
  }

  def "getOrCreate a value multiple times same class loader same key"() {
    when:
    def count1 = sut.get('key', supplier)
    def count2 = sut.get('key', supplier)

    then:
    count1 == 1
    count2 == 1
    supplier.counter == 1
    sut.cache.size() == 1
  }

  def "getOrCreate a value multiple times same class loader different keys"() {
    when:
    def count1 = sut.get('key1', supplier)
    def count2 = sut.get('key2', supplier)

    then:
    count1 == 1
    count2 == 2
    supplier.counter == 2
    sut.cache.size() == 2
  }

  class CounterSupplier implements Callable<Integer> {
    def counter = 0
    @Override
    Integer call() {
      counter = counter + 1
      return counter
    }
  }
}
