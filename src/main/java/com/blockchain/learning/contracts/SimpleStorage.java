package com.blockchain.learning.contracts;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * SimpleStorage智能合约的Java包装类
 *
 * 这个类是通过Web3j工具生成的智能合约包装类，提供了与SimpleStorage合约交互的Java方法。
 * 包含了合约的所有公共方法和事件的Java映射。
 *
 * @author Blockchain Learning Project
 * @version 1.0
 */
public class SimpleStorage extends Contract {

    // 合约的二进制代码（字节码）
    public static final String BINARY = "0x608060405234801561001057600080fd5b50600080819055503373ffffffffffffffffffffffffffffffffffffffff16600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055507f4b4b0c8e0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b600080546040516100d59291906100db565b60405180910390a1610104565b6100f8828261010a565b82525050565b60006020820190506101136000830184610104565b92915050565b610a8d806101286000396000f3fe608060405234801561001057600080fd5b50600436106100885760003560e01c80636057361d1161005b5780636057361d146100f15780638da5cb5b1461010d578063a2fb11751461012b578063f2fde38b1461014957610088565b80630c55699c1461008d5780632e64cec1146100ab5780634f2be91f146100c95780635b9af12b146100d3575b600080fd5b610095610165565b6040516100a291906109c4565b60405180910390f35b6100b361016b565b6040516100c091906109c4565b60405180910390f35b6100d1610174565b005b6100eb60048036038101906100e691906108e7565b6101c8565b6040516100f891906109c4565b60405180910390f35b61010b600480360381019061010691906108e7565b610228565b005b610115610285565b6040516101229190610949565b60405180910390f35b6101336102ab565b6040516101409190610a0f565b60405180910390f35b610163600480360381019061015e91906108ba565b6102f1565b005b60005481565b60008054905090565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16146101c657600080fd5b565b6000806000549050826000808282546101e09190610a31565b925050819055507f4b4b0c8e0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b6000548260405161021a929190610a87565b60405180910390a160005491505b50919050565b6000805490508160008190555050507f4b4b0c8e0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b6000548260405161027a929190610a87565b60405180910390a15050565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60008060008054600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1630925092509250909192565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161461034b57600080fd5b600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16141561038557600080fd5b8073ffffffffffffffffffffffffffffffffffffffff16600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a380600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b60008135905061045a81610a29565b92915050565b60008135905061046f81610a40565b92915050565b60006020828403121561048757600080fd5b60006104958482850161044b565b91505092915050565b6000602082840312156104b057600080fd5b60006104be84828501610460565b91505092915050565b6104d081610ab0565b82525050565b6104df81610ac2565b82525050565b60006104f082610a87565b6104fa8185610a9c565b935061050a818560208601610ace565b61051381610b01565b840191505092915050565b600061052b602683610a9c565b915061053682610b12565b604082019050919050565b600061054e601983610a9c565b915061055982610b61565b602082019050919050565b6000610571602083610a9c565b915061057c82610b8a565b602082019050919050565b61059081610ab0565b82525050565b60006020820190506105ab60008301846104c7565b92915050565b60006020820190506105c660008301846104d6565b92915050565b600060208201905081810360008301526105e681846104e5565b905092915050565b600060208201905081810360008301526106078161051e565b9050919050565b6000602082019050818103600083015261062781610541565b9050919050565b6000602082019050818103600083015261064781610564565b9050919050565b60006020820190506106636000830184610587565b92915050565b600060608201905061067e6000830186610587565b61068b60208301856104c7565b61069860408301846104c7565b949350505050565b60006106aa6106bb565b90506106b68282610b33565b919050565b6000604051905090565b600067ffffffffffffffff8211156106e0576106df610bd2565b5b6106e982610c01565b9050602081019050919050565b600081519050919050565b600082825260208201905092915050565b600061071d82610ab0565b915061072883610ab0565b9250827fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff0382111561075d5761075c610b64565b5b828201905092915050565b600061077382610ab0565b915061077e83610ab0565b92508282101561079157610790610b64565b5b828203905092915050565b60006107a782610a90565b9050919050565b60008115159050919050565b6000819050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000819050919050565b82818337600083830152505050565b60005b8381101561081c578082015181840152602081019050610801565b8381111561082b576000848401525b50505050565b6000600282049050600182168061084957607f821691505b6020821081141561085d5761085c610ba3565b5b50919050565b61086c82610c01565b810181811067ffffffffffffffff8211171561088b5761088a610bd2565b5b80604052505050565b600061089f82610ab0565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8214156108d2576108d1610b64565b5b600182019050919050565b6108e68161079c565b81146108f157600080fd5b50565b6108fd816107ae565b811461090857600080fd5b50565b610914816107ba565b811461091f57600080fd5b50565b61092b816107e4565b811461093657600080fd5b5056fea2646970667358221220c4c4c4c4c4c4c4c4c4c4c4c4c4c4c4c4c4c4c4c4c4c4c4c4c4c4c4c4c4c4c4c464736f6c63430008070033";

    // Gas限制
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_700_000L);

    // Gas价格
    public static final BigInteger GAS_PRICE = BigInteger.valueOf(20_000_000_000L);

    // 事件定义
    public static final Event DATASTORED_EVENT = new Event("DataStored",
            Arrays.<TypeReference<?>>asList(
                    new TypeReference<Uint256>(true) {},
                    new TypeReference<Uint256>(true) {},
                    new TypeReference<Address>(true) {}));

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred",
            Arrays.<TypeReference<?>>asList(
                    new TypeReference<Address>(true) {},
                    new TypeReference<Address>(true) {}));

    /**
     * 构造函数
     */
    protected SimpleStorage(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SimpleStorage(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    /**
     * 获取存储的数值
     *
     * @return 当前存储的数值
     */
    public RemoteFunctionCall<BigInteger> get() {
        final Function function = new Function("get",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    /**
     * 设置存储的数值
     *
     * @param x 要存储的新数值
     * @return 交易回执
     */
    public RemoteFunctionCall<TransactionReceipt> set(BigInteger x) {
        final Function function = new Function(
                "set",
                Arrays.<Type>asList(new Uint256(x)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    /**
     * 增加存储的数值
     *
     * @param increment 要增加的数量
     * @return 增加后的新值
     */
    public RemoteFunctionCall<BigInteger> increment(BigInteger increment) {
        final Function function = new Function("increment",
                Arrays.<Type>asList(new Uint256(increment)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    /**
     * 重置存储的数值为0
     * 只有合约拥有者可以调用
     *
     * @return 交易回执
     */
    public RemoteFunctionCall<TransactionReceipt> reset() {
        final Function function = new Function(
                "reset",
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    /**
     * 获取合约拥有者地址
     *
     * @return 拥有者地址
     */
    public RemoteFunctionCall<String> owner() {
        final Function function = new Function("owner",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * 转移合约所有权
     *
     * @param newOwner 新拥有者地址
     * @return 交易回执
     */
    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final Function function = new Function(
                "transferOwnership",
                Arrays.<Type>asList(new Address(newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    /**
     * 获取合约信息
     *
     * @return 包含当前值、拥有者地址、合约地址的元组
     */
    public RemoteFunctionCall<Tuple3<BigInteger, String, String>> getInfo() {
        final Function function = new Function("getInfo",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(
                        new TypeReference<Uint256>() {},
                        new TypeReference<Address>() {},
                        new TypeReference<Address>() {}));
        return new RemoteFunctionCall<Tuple3<BigInteger, String, String>>(function,
                () -> {
                    List<Type> results = executeCallMultipleValueReturn(function);
                    return new Tuple3<BigInteger, String, String>(
                            (BigInteger) results.get(0).getValue(),
                            (String) results.get(1).getValue(),
                            (String) results.get(2).getValue());
                });
    }

    /**
     * 部署合约
     *
     * @param web3j Web3j实例
     * @param credentials 部署者凭证
     * @param contractGasProvider Gas提供者
     * @return 部署的合约实例
     */
    public static RemoteCall<SimpleStorage> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SimpleStorage.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    /**
     * 部署合约（使用默认Gas设置）
     *
     * @param web3j Web3j实例
     * @param credentials 部署者凭证
     * @param gasPrice Gas价格
     * @param gasLimit Gas限制
     * @return 部署的合约实例
     */
    public static RemoteCall<SimpleStorage> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SimpleStorage.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    /**
     * 加载已部署的合约
     *
     * @param contractAddress 合约地址
     * @param web3j Web3j实例
     * @param credentials 调用者凭证
     * @param gasPrice Gas价格
     * @param gasLimit Gas限制
     * @return 合约实例
     */
    public static SimpleStorage load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SimpleStorage(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    /**
     * 加载已部署的合约（使用TransactionManager）
     *
     * @param contractAddress 合约地址
     * @param web3j Web3j实例
     * @param transactionManager 交易管理器
     * @param gasPrice Gas价格
     * @param gasLimit Gas限制
     * @return 合约实例
     */
    public static SimpleStorage load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SimpleStorage(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}